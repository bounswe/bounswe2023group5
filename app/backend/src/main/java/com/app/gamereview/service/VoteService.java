package com.app.gamereview.service;

import com.app.gamereview.dto.request.notification.CreateNotificationRequestDto;
import com.app.gamereview.dto.request.vote.CreateVoteRequestDto;
import com.app.gamereview.dto.request.vote.GetAllVotesFilterRequestDto;
import com.app.gamereview.enums.NotificationMessage;
import com.app.gamereview.enums.NotificationParent;
import com.app.gamereview.enums.VoteChoice;
import com.app.gamereview.enums.VoteType;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.*;
import com.app.gamereview.repository.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class VoteService {
    private final VoteRepository voteRepository;

    private final ReviewRepository reviewRepository;

    private final ProfileRepository profileRepository;

    private final AchievementRepository achievementRepository;

    private final MongoTemplate mongoTemplate;

    private final ModelMapper modelMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;


    @Autowired
    public VoteService(
            VoteRepository voteRepository,
            ReviewRepository reviewRepository,
            ProfileRepository profileRepository,
            AchievementRepository achievementRepository,
            NotificationRepository notificationRepository,
            UserRepository userRepository,
            MongoTemplate mongoTemplate,
            ModelMapper modelMapper,
            PostRepository postRepository,
            CommentRepository commentRepository,
            NotificationService notificationService) {
        this.voteRepository = voteRepository;
        this.reviewRepository = reviewRepository;
        this.profileRepository = profileRepository;
        this.achievementRepository = achievementRepository;
        this.notificationRepository = notificationRepository;
        this.mongoTemplate = mongoTemplate;
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;


        modelMapper.addMappings(new PropertyMap<CreateVoteRequestDto, Vote>() {
            @Override
            protected void configure() {
                map().setTypeId(source.getTypeId());
                skip().setId(null); // Exclude id from mapping
            }
        });
    }

    public List<Vote> getAllVotes(GetAllVotesFilterRequestDto filter) {
        Query query = new Query();
        if (filter.getVoteType() != null) {
            query.addCriteria(Criteria.where("voteType").is(filter.getVoteType()));
        }
        if (filter.getTypeId() != null) {
            query.addCriteria(Criteria.where("typeId").is(filter.getTypeId()));
        }
        if (filter.getChoice() != null) {
            query.addCriteria(Criteria.where("choice").is(filter.getChoice()));
        }
        if (filter.getVotedBy() != null) {
            query.addCriteria(Criteria.where("votedBy").is(filter.getVotedBy()));
        }
        if (!filter.getWithDeleted()) {
            query.addCriteria(Criteria.where("isDeleted").is(filter.getWithDeleted()));
        }

        return mongoTemplate.find(query, Vote.class);
    }

    public Vote getVote(String voteId) {
        Optional<Vote> vote = voteRepository.findById(voteId);

        if (vote.isEmpty() || vote.get().getIsDeleted()) {
            throw new ResourceNotFoundException("Vote not found");
        }

        return vote.get();
    }

    public Vote addVote(CreateVoteRequestDto requestDto, User user) {
        Vote voteToCreate = modelMapper.map(requestDto, Vote.class);
        voteToCreate.setVotedBy(user.getId());

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(user.getId()));
        Profile profile = mongoTemplate.findOne(query, Profile.class);

        if(profile == null){
            throw new ResourceNotFoundException("Profile of the user does not exist");
        }

        if(!profile.getIsVotedYet()){      // first vote of the user
            Optional<Achievement> achievement =
                    achievementRepository.findByIdAndIsDeletedFalse("eb558639-32ca-413f-b842-c3788287dd05");
            achievement.ifPresent(value -> profile.addAchievement(value.getId()));
            profile.setIsVotedYet(true);
            CreateNotificationRequestDto createNotificationRequestDto= new CreateNotificationRequestDto();
            String message = NotificationMessage.FIRST_VOTE_ACHIEVEMENT.getMessageTemplate()
                    .replace("{user_name}", user.getUsername());
            createNotificationRequestDto.setMessage(message);
            createNotificationRequestDto.setParentType(NotificationParent.ACHIEVEMENT);
            createNotificationRequestDto.setUser(user.getId());
            notificationService.createNotification(createNotificationRequestDto);
        }


        GetAllVotesFilterRequestDto filter = new GetAllVotesFilterRequestDto();
        filter.setVotedBy(user.getId());
        filter.setTypeId(requestDto.getTypeId());
        filter.setVoteType(requestDto.getVoteType());
        List<Vote> prevVote = getAllVotes(filter);

        VoteChoice choice = VoteChoice.valueOf(requestDto.getChoice());

        // if user has voted this same thing before with the SAME CHOICE
        if (!prevVote.isEmpty() &&
                prevVote.get(0).getChoice().name().equals(requestDto.getChoice())) {

            profile.setVoteCount(profile.getVoteCount() - 1);
            profileRepository.save(profile);

            if (requestDto.getVoteType().equals(VoteType.REVIEW.name())) {
                // delete previous vote
                Review review = reviewRepository.findById(requestDto.getTypeId()).get();
                review.deleteVote(choice);
                reviewRepository.save(review);
                deleteVote(prevVote.get(0).getId());
                return prevVote.get(0);
            } else if (requestDto.getVoteType().equals(VoteType.POST.name())) {
                // delete previous vote
                Optional<Post> optionalPost = postRepository.findById(requestDto.getTypeId());
                if (optionalPost.isEmpty()) {
                    throw new ResourceNotFoundException("Post with the given Id is not found.");
                }
                Post post = optionalPost.get();
                post.deleteVote(choice);
                postRepository.save(post);
                deleteVote(prevVote.get(0).getId());
                return prevVote.get(0);
            } else if (requestDto.getVoteType().equals(VoteType.COMMENT.name())) {
                // delete previous vote
                Optional<Comment> optionalComment = commentRepository.findById(requestDto.getTypeId());
                if (optionalComment.isEmpty()) {
                    throw new ResourceNotFoundException("Comment with the given Id is not found.");
                }
                Comment comment = optionalComment.get();
                comment.deleteVote(choice);
                commentRepository.save(comment);
                deleteVote(prevVote.get(0).getId());
                return prevVote.get(0);
            }

            // TODO same logic will be extended to other models that have voting mechanism

        }
        // if user has voted this same thing before but CHANGED HIS CHOICE
        else if (!prevVote.isEmpty() &&
                !prevVote.get(0).getChoice().name().equals(requestDto.getChoice())) {

            profileRepository.save(profile);

            if (requestDto.getVoteType().equals(VoteType.REVIEW.name())) {
                // delete previous vote
                Review review = reviewRepository.findById(requestDto.getTypeId()).get();
                review.deleteVote(prevVote.get(0).getChoice());
                deleteVote(prevVote.get(0).getId());

                // add new vote
                review.addVote(choice);

                reviewRepository.save(review);
                return voteRepository.save(voteToCreate);
            } else if (requestDto.getVoteType().equals(VoteType.POST.name())) {
                // delete previous vote
                Optional<Post> optionalPost = postRepository.findById(requestDto.getTypeId());
                if (optionalPost.isEmpty()) {
                    throw new ResourceNotFoundException("Post with the given Id is not found.");
                }
                Post post = optionalPost.get();
                post.deleteVote(prevVote.get(0).getChoice());
                deleteVote(prevVote.get(0).getId());

                // add new vote
                post.addVote(choice);

                postRepository.save(post);
                return voteRepository.save(voteToCreate);
            } else if (requestDto.getVoteType().equals(VoteType.COMMENT.name())) {
                // delete previous vote
                Optional<Comment> optionalComment = commentRepository.findById(requestDto.getTypeId());
                if (optionalComment.isEmpty()) {
                    throw new ResourceNotFoundException("Comment with the given Id is not found.");
                }
                Comment comment = optionalComment.get();
                comment.deleteVote(prevVote.get(0).getChoice());
                deleteVote(prevVote.get(0).getId());

                // add new vote
                comment.addVote(choice);

                commentRepository.save(comment);
                return voteRepository.save(voteToCreate);
            }

            // TODO same logic will be extended to other models that have voting mechanism

        }
        // user votes something first time
        else {

            profile.setVoteCount(profile.getVoteCount() + 1);
            profileRepository.save(profile);

            if (requestDto.getVoteType().equals(VoteType.REVIEW.name())) {
                // add new vote
                Review review = reviewRepository.findById(requestDto.getTypeId()).get();
                review.addVote(choice);
                reviewRepository.save(review);
                return voteRepository.save(voteToCreate);
            } else if (requestDto.getVoteType().equals(VoteType.POST.name())) {
                // add new vote
                Optional<Post> optionalPost = postRepository.findById(requestDto.getTypeId());
                if (optionalPost.isEmpty()) {
                    throw new ResourceNotFoundException("The post with the given Id is not found.");
                }
                Post post = optionalPost.get();
                post.addVote(choice);
                postRepository.save(post);


                if(post.getVoteCount() == 1 && notificationRepository.findByParentAndUser(post.getId(), user.getId()).isEmpty()) {
                    CreateNotificationRequestDto createNotificationRequestDto= new CreateNotificationRequestDto();
                    Optional<User> optionalUser = userRepository.findById(post.getPoster());
                    if(optionalUser.isPresent()) {
                        User poster = optionalUser.get();
                        String message = NotificationMessage.FIRST_VOTE_OF_THE_POST.getMessageTemplate()
                                .replace("{user_name}", poster.getUsername())
                                .replace("{post_title}", post.getTitle());
                        createNotificationRequestDto.setMessage(message);
                        createNotificationRequestDto.setParentType(NotificationParent.POST);
                        createNotificationRequestDto.setParent(post.getId());
                        createNotificationRequestDto.setUser(post.getPoster());
                        notificationService.createNotification(createNotificationRequestDto);
                    }
                }

                List<Integer> notificationVoteCounts = new ArrayList<>(Arrays.asList(5, 10, 15));
                if(notificationVoteCounts.contains(post.getOverallVote())) {
                    CreateNotificationRequestDto createNotificationRequestDto= new CreateNotificationRequestDto();
                    Optional<User> optionalUser = userRepository.findById(post.getPoster());
                    if(optionalUser.isPresent()) {
                        User poster = optionalUser.get();
                        String message = NotificationMessage.NTH_VOTE_OF_THE_POST.getMessageTemplate()
                                .replace("{user_name}", poster.getUsername())
                                .replace("{post_title}", post.getTitle())
                                .replace("{overall_vote}", String.valueOf(post.getOverallVote()));
                        createNotificationRequestDto.setMessage(message);
                        createNotificationRequestDto.setParentType(NotificationParent.POST);
                        createNotificationRequestDto.setParent(post.getId());
                        createNotificationRequestDto.setUser(post.getPoster());
                        notificationService.createNotification(createNotificationRequestDto);
                    }
                }

                return voteRepository.save(voteToCreate);
            } else if (requestDto.getVoteType().equals(VoteType.COMMENT.name())) {
                // delete previous vote
                Optional<Comment> optionalComment = commentRepository.findById(requestDto.getTypeId());
                if (optionalComment.isEmpty()) {
                    throw new ResourceNotFoundException("Comment with the given Id is not found.");
                }
                Comment comment = optionalComment.get();
                comment.addVote(choice);
                commentRepository.save(comment);
                return voteRepository.save(voteToCreate);
            }

            // TODO same logic will be extended to other models that have voting mechanism

        }

        return voteToCreate;
    }

    public Boolean deleteVote(String voteId) {
        Optional<Vote> findResult = voteRepository.findById(voteId);

        if (findResult.isEmpty()) {
            throw new ResourceNotFoundException("Vote not found");
        }

        voteRepository.deleteById(voteId);

        return true;
    }

}
