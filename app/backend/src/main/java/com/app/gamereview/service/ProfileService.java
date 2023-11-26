package com.app.gamereview.service;

import com.app.gamereview.dto.request.profile.EditProfileRequestDto;
import com.app.gamereview.dto.request.profile.ProfileUpdateGameRequestDto;
import com.app.gamereview.dto.request.review.GetAllReviewsFilterRequestDto;
import com.app.gamereview.dto.request.vote.GetAllVotesFilterRequestDto;
import com.app.gamereview.dto.response.game.GameDetailResponseDto;
import com.app.gamereview.dto.response.profile.GetLastActivitiesResponseDto;
import com.app.gamereview.dto.response.profile.ProfilePageResponseDto;
import com.app.gamereview.dto.response.review.GetAllReviewsResponseDto;
import com.app.gamereview.enums.ActivityType;
import com.app.gamereview.enums.UserRole;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.*;
import com.app.gamereview.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final AchievementRepository achievementRepository;
    private final ReviewRepository reviewRepository;
    private final GroupRepository groupRepository;
    private final ReviewService reviewService;
    private final VoteService voteService;
    private final CommentService commentService;
    private final PostService postService;
    private final GameService gameService;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository, GameRepository gameRepository, AchievementRepository achievementRepository, ReviewRepository reviewRepository, GroupRepository groupRepository, ReviewService reviewService, VoteService voteService, CommentService commentService, PostService postService, GameService gameService) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.achievementRepository = achievementRepository;
        this.reviewRepository = reviewRepository;
        this.groupRepository = groupRepository;
        this.reviewService = reviewService;
        this.voteService = voteService;
        this.commentService = commentService;
        this.postService = postService;
        this.gameService = gameService;
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
            activity.setParentType("GAME");
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
            if (comment.getParentComment() == null) {
                activity.setParentType("POST");
            } else {
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

    public Profile editProfile(String id, EditProfileRequestDto request, User user) {
        Optional<Profile> profile = profileRepository.findById(id);

        if (profile.isEmpty() || profile.get().getIsDeleted()) {
            throw new ResourceNotFoundException("The profile with the given id is not found.");
        }

        if (!(profile.get().getUserId().equals(user.getId()) || UserRole.ADMIN.equals(user.getRole()))) {
            throw new BadRequestException("Only the user who is the owner of the profile or admin can edit it.");
        }

        Profile editedProfile = profile.get();

        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            Optional<User> userToUpdate = userRepository.findByUsername(request.getUsername());
            if (userToUpdate.isPresent()) {
                throw new BadRequestException("Requested username is not available.");
            }
            user.setUsername(request.getUsername());
        }

        if (request.getIsPrivate() != null) {
            editedProfile.setIsPrivate(request.getIsPrivate());
        }

        if (request.getProfilePhoto() != null && !request.getProfilePhoto().isEmpty()) {
            editedProfile.setProfilePhoto(request.getProfilePhoto());
        }

        if (request.getSteamProfile() != null && !request.getSteamProfile().isEmpty()) {
            editedProfile.setSteamProfile(request.getSteamProfile());
        }

        if (request.getEpicGamesProfile() != null && !request.getEpicGamesProfile().isEmpty()) {
            editedProfile.setEpicGamesProfile(request.getEpicGamesProfile());
        }

        if (request.getXboxProfile() != null && !request.getXboxProfile().isEmpty()) {
            editedProfile.setXboxProfile(request.getXboxProfile());
        }
        userRepository.save(user);
        return profileRepository.save(editedProfile);
    }

    public Profile addGameToProfile(String id, ProfileUpdateGameRequestDto request, User user) {
        Optional<Profile> profile = profileRepository.findById(id);

        if (profile.isEmpty() || profile.get().getIsDeleted()) {
            throw new ResourceNotFoundException("The profile with the given id is not found.");
        }

        if (!(profile.get().getUserId().equals(user.getId()) || UserRole.ADMIN.equals(user.getRole()))) {
            throw new BadRequestException("Only the user who is the owner of the profile or admin can add a game.");
        }

        Profile editedProfile = profile.get();

        Optional<Game> gameToAdd = gameRepository.findById(request.getGame());
        if (gameToAdd.isEmpty() || gameToAdd.get().getIsDeleted()) {
            throw new ResourceNotFoundException("The game with the given id is not found.");
        }

        editedProfile.addGame(request.getGame());

        return profileRepository.save(editedProfile);
    }

    public Profile removeGameFromProfile(String id, ProfileUpdateGameRequestDto request, User user) {
        Optional<Profile> profile = profileRepository.findById(id);

        if (profile.isEmpty() || profile.get().getIsDeleted()) {
            throw new ResourceNotFoundException("The profile with the given id is not found.");
        }

        if (!(profile.get().getUserId().equals(user.getId()) || UserRole.ADMIN.equals(user.getRole()))) {
            throw new BadRequestException("Only the user who is the owner of the profile or admin can remove a game.");
        }

        Profile editedProfile = profile.get();

        editedProfile.removeGame(request.getGame());

        return profileRepository.save(editedProfile);
    }

    public ProfilePageResponseDto getProfile(String userId, String email) {
        Optional<Profile> optionalProfile = profileRepository.findByUserIdAndIsDeletedFalse(userId);

        if (optionalProfile.isEmpty() || optionalProfile.get().getIsDeleted()) {
            throw new ResourceNotFoundException("The profile for the given user id is not found.");
        }

        Profile profile = optionalProfile.get();
        if (profile.getIsPrivate()) {
            Optional<User> optionalUser = userRepository.findByEmailAndIsDeletedFalse(email);
            if (optionalUser.isEmpty()) {
                throw new BadRequestException("The profile is private and no valid authorization is given");
            }
            User user = optionalUser.get();
            if (!(profile.getUserId().equals(user.getId()) || UserRole.ADMIN.equals(user.getRole()))) {
                throw new BadRequestException("Only the user who is the owner of the profile or admin can see the details of a private profile.");
            }
        }
        return convertToProfilePageDto(profile);
    }

    private ProfilePageResponseDto convertToProfilePageDto(Profile profile) {
        String userId = profile.getUserId();
        Optional<User> user = userRepository.findByIdAndIsDeletedFalse(userId);
        User userObject = user.orElse(null);

        List<String> achievements = profile.getAchievements();
        List<Achievement> achivementObjects = new ArrayList<>();

        for (String achievementId : achievements) {
            achievementRepository.findById(achievementId).ifPresent(achivementObjects::add);
        }

        List<String> games = profile.getGames();
        List<GameDetailResponseDto> gameObjects = new ArrayList<>();

        for (String gameId : games) {
            gameObjects.add(gameService.getGameDetail(gameId));
        }

        List<Review> reviewObjects = reviewRepository.findByReviewedBy(userId);
        List<Group> groupObjects = groupRepository.findUserGroups(userId);

        return new ProfilePageResponseDto(profile.getId(), userObject, achivementObjects, profile.getReviewCount(),
                profile.getVoteCount(), profile.getCommentCount(), profile.getPostCount(), profile.getIsReviewedYet(),
                profile.getIsVotedYet(), profile.getIsCommentedYet(), profile.getIsPostedYet(), profile.getIsPrivate(),
                profile.getProfilePhoto(), gameObjects, reviewObjects, groupObjects, profile.getSteamProfile(),
                profile.getEpicGamesProfile(), profile.getXboxProfile());
    }

}
