package com.app.gamereview.service;

import com.app.gamereview.dto.request.review.GetAllReviewsFilterRequestDto;
import com.app.gamereview.dto.request.vote.GetAllVotesFilterRequestDto;
import com.app.gamereview.dto.response.profile.GetLastActivitiesResponseDto;
import com.app.gamereview.dto.response.review.GetAllReviewsResponseDto;
import com.app.gamereview.enums.ActivityType;
import com.app.gamereview.model.Comment;
import com.app.gamereview.model.Post;
import com.app.gamereview.model.User;
import com.app.gamereview.model.Vote;
import com.app.gamereview.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    private final ReviewService reviewService;

    private final VoteService voteService;
    private final CommentService commentService;
    private final PostService postService;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, ReviewService reviewService, VoteService voteService, CommentService commentService, PostService postService){
        this.profileRepository = profileRepository;
        this.reviewService = reviewService;
        this.voteService = voteService;
        this.commentService = commentService;
        this.postService = postService;
    }

    public List<GetLastActivitiesResponseDto> getLastActivities(User user) {
        GetAllReviewsFilterRequestDto reviewFilter = new GetAllReviewsFilterRequestDto();
        reviewFilter.setReviewedBy(user.getId());
        reviewFilter.setSortBy("CREATION_DATE");
        List<GetAllReviewsResponseDto> reviews = reviewService.getAllReviews(reviewFilter, user.getEmail());

        GetAllVotesFilterRequestDto voteFilter = new GetAllVotesFilterRequestDto();
        voteFilter.setVotedBy(user.getId());
        List<Vote> votes = voteService.getAllVotes(voteFilter);

        List<Comment> comments = commentService.getUserCommentList(user);

        List<Post> posts = postService.getUserPostList(user);

        List<GetLastActivitiesResponseDto> lastActivities = new ArrayList<>();

        for (GetAllReviewsResponseDto review : reviews) {
            GetLastActivitiesResponseDto activity = new GetLastActivitiesResponseDto();
            activity.setTypeId(review.getId());
            activity.setParentId(review.getGameId());
            activity.setType(ActivityType.REVIEW);
            activity.setDescription(review.getReviewDescription());
            activity.setCreatedAt(review.getCreatedAt());
            lastActivities.add(activity);
        }

        for (Vote vote : votes) {
            GetLastActivitiesResponseDto activity = new GetLastActivitiesResponseDto();
            activity.setTypeId(vote.getId());
            activity.setType(ActivityType.VOTE);
            activity.setParentId(vote.getTypeId());
            activity.setParentType(vote.getVoteType().name());
            activity.setDescription(vote.getChoice().name());
            activity.setCreatedAt(vote.getCreatedAt());
            lastActivities.add(activity);
        }

        for (Comment comment : comments) {
            GetLastActivitiesResponseDto activity = new GetLastActivitiesResponseDto();
            activity.setTypeId(comment.getId());
            activity.setType(ActivityType.COMMENT);
            activity.setParentId(comment.getPost());
            activity.setDescription(comment.getCommentContent());
            activity.setCreatedAt(comment.getCreatedAt());
            if(comment.getParentComment()== null){
                activity.setParentType("POST");
            }
            else{
                activity.setParentType("COMMENT");
            }
            lastActivities.add(activity);
        }

        for (Post post : posts) {
            GetLastActivitiesResponseDto activity = new GetLastActivitiesResponseDto();
            activity.setTypeId(post.getId());
            activity.setType(ActivityType.POST);
            activity.setParentId(post.getForum());
            activity.setDescription(post.getPostContent());
            activity.setCreatedAt(post.getCreatedAt());
            lastActivities.add(activity);
        }

        // Sort the activities by createdAt in descending order
        lastActivities.sort(Comparator.comparing(GetLastActivitiesResponseDto::getCreatedAt).reversed());

        return lastActivities;
    }



}
