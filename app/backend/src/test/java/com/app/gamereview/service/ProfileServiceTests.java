package com.app.gamereview.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.app.gamereview.dto.request.review.GetAllReviewsFilterRequestDto;
import com.app.gamereview.dto.request.vote.GetAllVotesFilterRequestDto;
import com.app.gamereview.dto.response.profile.GetLastActivitiesResponseDto;
import com.app.gamereview.dto.response.review.GetAllReviewsResponseDto;
import com.app.gamereview.enums.ActivityType;
import com.app.gamereview.enums.UserRole;
import com.app.gamereview.enums.VoteChoice;
import com.app.gamereview.enums.VoteType;
import com.app.gamereview.model.*;
import com.app.gamereview.repository.*;
import com.app.gamereview.dto.request.profile.EditProfileRequestDto;
import com.app.gamereview.dto.request.profile.ProfileUpdateGameRequestDto;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.exception.BadRequestException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ProfileServiceTests {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;
    @Mock
    private ReviewService reviewService;
    @Mock
    private VoteService voteService;

    @Mock
    private CommentService commentService;
    @Mock
    private PostService postService;

    @InjectMocks
    private ProfileService profileService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEditProfileSuccess() {
        String profileId = "profileId";
        String userId = "user1";
        Profile mockProfile = new Profile();
        mockProfile.setUserId(userId);

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(mockProfile));
        when(profileRepository.save(any(Profile.class))).thenReturn(mockProfile);

        User mockUser = new User();
        mockUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        EditProfileRequestDto request = new EditProfileRequestDto();
        Profile result = profileService.editProfile(profileId, request, mockUser);

        assertNotNull(result, "The result should not be null");
        assertEquals(mockProfile, result, "The returned profile should match the mock profile");
    }

    @Test
    public void testEditProfileNotFound() {
        when(profileRepository.findById(anyString())).thenReturn(Optional.empty());

        EditProfileRequestDto request = new EditProfileRequestDto();
        User user = new User();

        // Assert that ResourceNotFoundException is thrown
        assertThrows(ResourceNotFoundException.class, () -> {
            profileService.editProfile("invalidId", request, user);
        });
    }

    @Test
    public void testAddGameToProfile() {
        String profileId = "profileId";
        String gameId = "gameId";
        String userId = "user1";

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setRole(UserRole.ADMIN);  // or USER, if testing for user ownership

        Profile mockProfile = new Profile();
        mockProfile.setUserId(userId);
        mockProfile.setIsDeleted(false);  // Ensure profile is not marked as deleted
        when(profileRepository.findById(profileId)).thenReturn(Optional.of(mockProfile));

        Game mockGame = new Game();
        mockGame.setGameName("Test Game");
        mockGame.setIsDeleted(false);  // Ensure game is not marked as deleted
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(mockGame));

        ProfileUpdateGameRequestDto request = new ProfileUpdateGameRequestDto();
        request.setGame(gameId);

        profileService.addGameToProfile(profileId, request, mockUser);

        assertTrue(mockProfile.getGames().contains(gameId), "The game should be added to the profile");

        verify(profileRepository, times(1)).save(mockProfile);
    }

    @Test
    public void testRemoveGameFromProfile() {
        String profileId = "profileId";
        String gameId = "gameId";
        String userId = "userId";
        User mockUser = new User();
        mockUser.setId(userId);

        Profile mockProfile = new Profile();
        mockProfile.setUserId(userId);
        mockProfile.addGame(gameId);
        when(profileRepository.findById(profileId)).thenReturn(Optional.of(mockProfile));

        ProfileUpdateGameRequestDto request = new ProfileUpdateGameRequestDto();
        request.setGame(gameId);

        Profile result = profileService.removeGameFromProfile(profileId, request, mockUser);

        assertFalse(mockProfile.getGames().contains(gameId), "The game should be removed from the profile");

        verify(profileRepository, times(1)).save(mockProfile);
    }


    @Test
    public void testGetProfilePrivateProfileAccessDenied() {
        String profileUserId = "ownerId";
        String nonOwnerUserId = "nonOwnerId";

        Profile mockProfile = new Profile();
        mockProfile.setUserId(profileUserId);
        mockProfile.setIsPrivate(true);

        User nonOwnerUser = new User();
        nonOwnerUser.setId(nonOwnerUserId);
        nonOwnerUser.setRole(UserRole.BASIC);

        when(profileRepository.findByUserIdAndIsDeletedFalse(profileUserId)).thenReturn(Optional.of(mockProfile));

        assertThrows(BadRequestException.class, () -> {
            profileService.getProfile(profileUserId, nonOwnerUser.getEmail());
        }, "Expected BadRequestException for accessing a private profile by a non-owner and non-admin user");
    }
    @Test
    public void testEditProfileUsernameAlreadyTaken() {
        String profileId = "profileId";
        String userId = "user1";
        String existingUsername = "existingUsername";
        String newUsername = "newUsername";

        Profile mockProfile = new Profile();
        mockProfile.setUserId(userId);

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(mockProfile));

        User existingUser = new User();
        existingUser.setId("existingUserId");
        existingUser.setUsername(newUsername);
        when(userRepository.findByUsername(newUsername)).thenReturn(Optional.of(existingUser));

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername(existingUsername);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        EditProfileRequestDto request = new EditProfileRequestDto();
        request.setUsername(newUsername);

        assertThrows(BadRequestException.class, () -> {
            profileService.editProfile(profileId, request, mockUser);
        }, "Expected BadRequestException for attempting to use an already taken username");
    }


    @Test
    public void testGetProfileSuccess() {
        String userId = "userId";
        String email = "user@example.com";

        Profile mockProfile = new Profile();
        mockProfile.setUserId(userId);
        mockProfile.setIsPrivate(false);

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail(email);

        when(profileRepository.findByUserIdAndIsDeletedFalse(userId)).thenReturn(Optional.of(mockProfile));
        when(userRepository.findByEmailAndIsDeletedFalse(email)).thenReturn(Optional.of(mockUser));

        profileService.getProfile(userId, email);

        // Assertions
        assertEquals(userId, mockProfile.getUserId(), "The profile should match the requested user id");
        assertFalse(mockProfile.getIsPrivate(), "The profile should be public");
    }
    @Test
    public void testGetLastActivities() {
        User user = new User();
        user.setId("userId");
        user.setEmail("user@example.com");

        GetAllReviewsFilterRequestDto reviewFilter = new GetAllReviewsFilterRequestDto();
        reviewFilter.setReviewedBy(user.getId());
        reviewFilter.setSortBy("CREATION_DATE");

        GetAllVotesFilterRequestDto voteFilter = new GetAllVotesFilterRequestDto();
        voteFilter.setVotedBy(user.getId());

        GetAllReviewsResponseDto mockReview = new GetAllReviewsResponseDto();
        mockReview.setId("reviewId");
        mockReview.setGameId("gameId");
        mockReview.setReviewDescription("This is a review");
        mockReview.setCreatedAt(LocalDateTime.parse("2023-01-01T12:00:00"));

        Vote mockVote = new Vote();
        mockVote.setId("voteId");
        mockVote.setTypeId("reviewId");
        mockVote.setVoteType(VoteType.REVIEW);
        mockVote.setChoice(VoteChoice.UPVOTE);
        mockVote.setVotedBy(user.getId());
        mockVote.setCreatedAt(LocalDateTime.parse("2023-01-02T12:00:00"));

        Comment mockComment = new Comment();
        mockComment.setId("commentId");
        mockComment.setPost("postId");
        mockComment.setCommentContent("This is a comment");
        mockComment.setCreatedAt(LocalDateTime.parse("2023-01-04T12:00:00"));

        // Mock post data
        Post mockPost = new Post();
        mockPost.setId("postId");
        mockPost.setForum("forumId");
        mockPost.setPostContent("This is a post");
        mockPost.setCreatedAt(LocalDateTime.parse("2023-01-05T12:00:00"));

        when(reviewService.getAllReviews(any(), any())).thenReturn(List.of(mockReview));
        when(voteService.getAllVotes(any())).thenReturn(List.of(mockVote));
        when(commentService.getUserCommentList(any())).thenReturn(List.of(mockComment));
        when(postService.getUserPostList(any())).thenReturn(List.of(mockPost));

        List<GetLastActivitiesResponseDto> lastActivities = profileService.getLastActivities(user);
        System.out.println(lastActivities);
        assertNotNull(lastActivities, "The result should not be null");
        assertEquals(4, lastActivities.size(), "There should be four activity");
        for (int i = 1; i < lastActivities.size(); i++) {
            GetLastActivitiesResponseDto currentActivity = lastActivities.get(i);
            GetLastActivitiesResponseDto previousActivity = lastActivities.get(i - 1);

            // Check that the current activity's creation date is equal to or after the previous one
            assertFalse(currentActivity.getCreatedAt().isAfter(previousActivity.getCreatedAt()), "Activities should be sorted in descending order based on creation date");
        }
    }
}
