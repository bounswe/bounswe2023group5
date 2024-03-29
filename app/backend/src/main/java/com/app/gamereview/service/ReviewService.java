package com.app.gamereview.service;

import com.app.gamereview.dto.request.notification.CreateNotificationRequestDto;
import com.app.gamereview.dto.request.review.CreateReviewRequestDto;
import com.app.gamereview.dto.request.review.GetAllReviewsFilterRequestDto;
import com.app.gamereview.dto.request.review.UpdateReviewRequestDto;
import com.app.gamereview.dto.response.review.GetAllReviewsResponseDto;
import com.app.gamereview.enums.*;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.*;
import com.app.gamereview.repository.*;
import com.mongodb.client.result.UpdateResult;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private final GameRepository gameRepository;

    private final VoteRepository voteRepository;

    private final AchievementRepository achievementRepository;

    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    private final MongoTemplate mongoTemplate;

    private final ModelMapper modelMapper;
    private final NotificationService notificationService;

    @Autowired
    public ReviewService(
            ReviewRepository reviewRepository,
            GameRepository gameRepository,
            VoteRepository voteRepository,
            AchievementRepository achievementRepository,
            UserRepository userRepository,
            ProfileRepository profileRepository,
            MongoTemplate mongoTemplate,
            ModelMapper modelMapper,
            NotificationService notificationService
    ) {
        this.reviewRepository = reviewRepository;
        this.gameRepository = gameRepository;
        this.voteRepository = voteRepository;
        this.achievementRepository = achievementRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.mongoTemplate = mongoTemplate;
        this.modelMapper = modelMapper;
        this.notificationService = notificationService;

        modelMapper.addMappings(new PropertyMap<CreateReviewRequestDto, Review>() {
            @Override
            protected void configure() {
                map().setGameId(source.getGameId());
                skip().setId(null); // Exclude Id from mapping
            }
        });
    }

    public List<GetAllReviewsResponseDto> getAllReviews(GetAllReviewsFilterRequestDto filter, String email) {
        Optional<User> loggedInUser = userRepository.findByEmailAndIsDeletedFalse(email);
        String loggedInUserId = loggedInUser.map(User::getId).orElse(null);

        Query query = new Query();
        if (filter.getGameId() != null) {
            query.addCriteria(Criteria.where("gameId").is(filter.getGameId()));
        }
        if (filter.getReviewedBy() != null) {
            query.addCriteria(Criteria.where("reviewedBy").is(filter.getReviewedBy()));
        }
        if (!filter.getWithDeleted()) {
            query.addCriteria(Criteria.where("isDeleted").is(filter.getWithDeleted()));
        }

        if (filter.getSortBy() != null) {
            Sort.Direction sortDirection = Sort.Direction.DESC;
            if (filter.getSortDirection() != null) {
                sortDirection = filter.getSortDirection().equals(SortDirection.ASCENDING.name()) ? Sort.Direction.ASC
                        : Sort.Direction.DESC;
            }
            if (filter.getSortBy().equals(SortType.CREATION_DATE.name())) {
                query.with(Sort.by(sortDirection, "createdAt"));
            }
            else if (filter.getSortBy().equals(SortType.OVERALL_VOTE.name())) {
                query.with(Sort.by(sortDirection, "overallVote"));
            }
            else if (filter.getSortBy().equals(SortType.VOTE_COUNT.name())) {
                query.with(Sort.by(sortDirection, "voteCount"));
            }
        }

        List<Review> filteredReviews =  mongoTemplate.find(query, Review.class);

        List<GetAllReviewsResponseDto> reviewDtos = new ArrayList<>();

        for(Review review : filteredReviews){
            GetAllReviewsResponseDto reviewDto = modelMapper.map(review, GetAllReviewsResponseDto.class);

            Optional<Vote> vote = voteRepository.findByVoteTypeAndTypeIdAndVotedBy("REVIEW",
                    review.getId(), loggedInUserId);

            if(vote.isPresent()){
                reviewDto.setRequestedUserVote(vote.get().getChoice().name());
            }

            reviewDto.setReviewedUser(userRepository.findById(review.getReviewedBy())
                    .get().getUsername());
            reviewDtos.add(reviewDto);
        }
        return reviewDtos;
    }

    public GetAllReviewsResponseDto getReview(String reviewId){
        Optional<Review> review = reviewRepository.findById(reviewId);

        if(review.isEmpty() || review.get().getIsDeleted()){
            throw new ResourceNotFoundException("Review not found");
        }

        GetAllReviewsResponseDto reviewDto = modelMapper.map(review, GetAllReviewsResponseDto.class);
        reviewDto.setReviewedUser(userRepository.findById(review.get().getReviewedBy())
                .get().getUsername());

        return reviewDto;
    }

    public Review addReview(CreateReviewRequestDto requestDto, User user){
        Review reviewToCreate = modelMapper.map(requestDto, Review.class);
        reviewToCreate.setReviewedBy(user.getId());

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(user.getId()));
        Profile profile = mongoTemplate.findOne(query, Profile.class);

        if(profile == null){
            throw new ResourceNotFoundException("Profile of the user does not exist");
        }

        // add game rating
        Game reviewedGame = gameRepository.findById(reviewToCreate.getGameId()).get();
        reviewedGame.addRating(reviewToCreate.getRating());
        gameRepository.save(reviewedGame);

        if(!profile.getIsReviewedYet()){      // first review of the user
            Optional<Achievement> achievement =
                    achievementRepository.findByIdAndIsDeletedFalse("405564b0-fbc0-4864-853c-c6e8e4cd2acd");
            achievement.ifPresent(value -> profile.addAchievement(value.getId()));
            profile.setIsReviewedYet(true);
            CreateNotificationRequestDto createNotificationRequestDto= new CreateNotificationRequestDto();
            String message = NotificationMessage.FIRST_REVIEW_ACHIEVEMENT.getMessageTemplate()
                    .replace("{user_name}", user.getUsername())
                    .replace("{game_name}", reviewedGame.getGameName());
            createNotificationRequestDto.setParentType(NotificationParent.ACHIEVEMENT);
            createNotificationRequestDto.setMessage(message);
            createNotificationRequestDto.setUser(user.getId());
            notificationService.createNotification(createNotificationRequestDto);
        }
        profile.setReviewCount(profile.getReviewCount() + 1);
        profileRepository.save(profile);

        reviewRepository.save(reviewToCreate);
        return reviewToCreate;
    }

    public Boolean updateReview(String reviewId, UpdateReviewRequestDto requestDto, User user){
        Optional<Review> findResult = reviewRepository.findById(reviewId);

        if (findResult.isEmpty() || findResult.get().getIsDeleted()){
            throw new ResourceNotFoundException("Review not found");
        }

        if (!(findResult.get().getReviewedBy().equals(user.getId()) || UserRole.ADMIN.equals(user.getRole()))) {
            throw new BadRequestException("Only the user that created the review or the admin can update it.");
        }
        float oldRating = findResult.get().getRating();
        float newRating = requestDto.getRating();

        Query query = new Query(Criteria.where("_id").is(reviewId));
        Update update = new Update();
        update.set("reviewDescription",requestDto.getReviewDescription());
        update.set("rating",requestDto.getRating());
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Review.class);

        // update game rating
        Game reviewedGame = gameRepository.findById(findResult.get().getGameId()).get();
        reviewedGame.updateRating(oldRating,newRating);
        gameRepository.save(reviewedGame);

        return updateResult.wasAcknowledged();
    }

    public Boolean deleteReview(String reviewId, User user){
        Optional<Review> findResult = reviewRepository.findById(reviewId);

        if(findResult.isEmpty()){
            throw new ResourceNotFoundException("Review not found");
        }
        if (!(findResult.get().getReviewedBy().equals(user.getId()) || UserRole.ADMIN.equals(user.getRole()))) {
            throw new BadRequestException("Only the user that created the review or the admin can delete it.");
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(user.getId()));
        Profile profile = mongoTemplate.findOne(query, Profile.class);

        if(profile == null){
            throw new ResourceNotFoundException("Profile of the user does not exist");
        }

        profile.setReviewCount(profile.getReviewCount() - 1);
        profileRepository.save(profile);

        // update game rating
        Game reviewedGame = gameRepository.findById(findResult.get().getGameId()).get();
        reviewedGame.deleteRating(findResult.get().getRating());
        gameRepository.save(reviewedGame);

        Review reviewToDelete = findResult.get();
        reviewToDelete.setIsDeleted(true);
        reviewRepository.save(reviewToDelete);
        return true;
    }

}
