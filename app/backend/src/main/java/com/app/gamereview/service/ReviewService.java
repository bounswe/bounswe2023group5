package com.app.gamereview.service;

import com.app.gamereview.dto.request.review.CreateReviewRequestDto;
import com.app.gamereview.dto.request.review.GetAllReviewsFilterRequestDto;
import com.app.gamereview.dto.request.review.UpdateReviewRequestDto;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.Game;
import com.app.gamereview.model.Review;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.GameRepository;
import com.app.gamereview.repository.ReviewRepository;
import com.mongodb.client.result.UpdateResult;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private final GameRepository gameRepository;

    private final MongoTemplate mongoTemplate;

    private final ModelMapper modelMapper;

    @Autowired
    public ReviewService(
            ReviewRepository reviewRepository,
            GameRepository gameRepository,
            MongoTemplate mongoTemplate,
            ModelMapper modelMapper
    ) {
        this.reviewRepository = reviewRepository;
        this.gameRepository = gameRepository;
        this.mongoTemplate = mongoTemplate;
        this.modelMapper = modelMapper;

        modelMapper.addMappings(new PropertyMap<CreateReviewRequestDto, Review>() {
            @Override
            protected void configure() {
                map().setGameId(source.getGameId());
                skip().setId(null); // Exclude Id from mapping
            }
        });
    }

    public List<Review> getAllReviews(GetAllReviewsFilterRequestDto filter) {
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

        return mongoTemplate.find(query, Review.class);
    }

    public Review getReview(String reviewId){
        Optional<Review> review = reviewRepository.findById(reviewId);

        if(review.isEmpty() || review.get().getIsDeleted()){
            throw new ResourceNotFoundException("Review not found");
        }

        return review.get();
    }

    public Review addReview(CreateReviewRequestDto requestDto, User user){
        Review reviewToCreate = modelMapper.map(requestDto, Review.class);
        reviewToCreate.setReviewedBy(user.getId());

        // add game rating
        Game reviewedGame = gameRepository.findById(reviewToCreate.getGameId()).get();
        reviewedGame.addRating(reviewToCreate.getRating());
        gameRepository.save(reviewedGame);

        reviewRepository.save(reviewToCreate);
        return reviewToCreate;
    }

    public Boolean updateReview(String reviewId, UpdateReviewRequestDto requestDto){
        Optional<Review> findResult = reviewRepository.findById(reviewId);

        if (findResult.isEmpty() || findResult.get().getIsDeleted()){
            throw new ResourceNotFoundException("Review not found");
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

    public Boolean deleteReview(String reviewId){
        Optional<Review> findResult = reviewRepository.findById(reviewId);

        if(findResult.isEmpty()){
            throw new ResourceNotFoundException("Review not found");
        }

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
