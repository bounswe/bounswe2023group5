package com.app.gamereview.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.app.gamereview.enums.UserRole;
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
}
