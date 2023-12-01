package com.app.gamereview.service;

import com.app.gamereview.dto.request.achievement.CreateAchievementRequestDto;
import com.app.gamereview.dto.request.achievement.GrantAchievementRequestDto;
import com.app.gamereview.dto.request.achievement.UpdateAchievementRequestDto;
import com.app.gamereview.enums.AchievementType;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.Achievement;
import com.app.gamereview.model.Game;
import com.app.gamereview.model.Profile;
import com.app.gamereview.repository.AchievementRepository;
import com.app.gamereview.repository.GameRepository;
import com.app.gamereview.repository.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AchievementServiceTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AchievementService achievementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAchievement_GameAchievement_Success() {
        // Arrange
        CreateAchievementRequestDto requestDto = new CreateAchievementRequestDto();
        requestDto.setType(AchievementType.GAME.toString());
        requestDto.setGame("gameId");
        Achievement achievement = new Achievement();
        achievement.setGame("gameId");
        achievement.setType(AchievementType.valueOf(requestDto.getType()));

        when(gameRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(new Game()));
        when(achievementRepository.findByTitleAndIsDeletedFalse(anyString())).thenReturn(new ArrayList<>());
        when(modelMapper.map(any(CreateAchievementRequestDto.class), eq(Achievement.class))).thenReturn(achievement);
        when(achievementRepository.save(any(Achievement.class))).thenReturn(achievement);
        // Act
        Achievement result = achievementService.createAchievement(requestDto);

        // Assert
        Assertions.assertNotNull(result);
        verify(achievementRepository, times(1)).save(any(Achievement.class));
    }

    @Test
    void testCreateAchievement_MetaAchievement_Success() {
        // Arrange
        CreateAchievementRequestDto requestDto = new CreateAchievementRequestDto();
        requestDto.setType(AchievementType.META.toString());
        Achievement achievement = new Achievement();
        achievement.setType(AchievementType.valueOf(requestDto.getType()));

        when(achievementRepository.findByTitleAndIsDeletedFalse(anyString())).thenReturn(new ArrayList<>());
        when(modelMapper.map(any(CreateAchievementRequestDto.class), eq(Achievement.class))).thenReturn(achievement);
        when(achievementRepository.save(any(Achievement.class))).thenReturn(achievement);

        // Act
        Achievement result = achievementService.createAchievement(requestDto);

        // Assert
        Assertions.assertNotNull(result);
        verify(achievementRepository, times(1)).save(any(Achievement.class));
    }


    @Test
    void testUpdateAchievement_Success() {
        // Arrange
        String achievementId = "achievementId";
        UpdateAchievementRequestDto requestDto = new UpdateAchievementRequestDto();
        requestDto.setTitle("New Title");
        Achievement existingAchievement = new Achievement();
        existingAchievement.setId(achievementId);
        when(achievementRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(existingAchievement));
        when(achievementRepository.findByTitleAndIsDeletedFalse(anyString())).thenReturn(Collections.emptyList());

        // Act
        Achievement result = achievementService.updateAchievement(achievementId, requestDto);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(requestDto.getTitle(), result.getTitle());
        verify(achievementRepository, times(1)).save(any(Achievement.class));
    }

    @Test
    void testDeleteAchievementById_Success() {
        // Arrange
        String achievementId = "achievementId";
        Achievement existingAchievement = new Achievement();
        existingAchievement.setId(achievementId);
        when(achievementRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(existingAchievement));

        // Act
        Achievement result = achievementService.deleteAchievement(achievementId);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getIsDeleted());
        verify(achievementRepository, times(1)).save(any(Achievement.class));
    }

    @Test
    void testDeleteAchievement_NotFoundById() {
        // Arrange
        String achievementId = "nonExistentAchievementId";

        when(achievementRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> achievementService.deleteAchievement(achievementId),
                "Should throw ResourceNotFoundException when achievement is not found by ID");

        // Verify
        verify(achievementRepository, times(1)).findByIdAndIsDeletedFalse(anyString());
    }

    @Test
    void testDeleteAchievementByNameAndGame_Success() {
        // Arrange
        String achievementName = "achievementName";
        String gameName = "gameName";
        Achievement existingAchievement = new Achievement();
        Game existingGame = new Game();

        existingAchievement.setTitle(achievementName);
        existingGame.setGameName(gameName);
        existingAchievement.setGame(existingGame.getId());

        when(achievementRepository.findByTitleAndIsDeletedFalse(anyString())).thenReturn(Collections.singletonList(existingAchievement));
        when(gameRepository.findByGameNameAndIsDeletedFalse(anyString())).thenReturn(Optional.of(existingGame));
        when(gameRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(existingGame));

        // Act
        Achievement result = achievementService.deleteAchievement(achievementName, gameName);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getIsDeleted());
        verify(achievementRepository, times(1)).save(any(Achievement.class));
    }

    @Test
    void testGrantAchievement_Success() {
        // Arrange
        String achievementId = "achievementId";
        String userId = "userId";
        GrantAchievementRequestDto requestDto = new GrantAchievementRequestDto();
        requestDto.setAchievementId(achievementId);
        requestDto.setUserId(userId);
        when(achievementRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(new Achievement()));
        when(profileRepository.findByUserIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(new Profile()));

        // Act
        List<String> result = achievementService.grantAchievement(requestDto);

        // Assert
        Assertions.assertNotNull(result);
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void testGetGameAchievements_Success() {
        // Arrange
        String gameId = "gameId";
        when(gameRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(new Game()));
        when(achievementRepository.findByGameAndIsDeletedFalse(anyString())).thenReturn(Collections.emptyList());

        // Act
        List<Achievement> result = achievementService.getGameAchievements(gameId);

        // Assert
        Assertions.assertNotNull(result);
        verify(achievementRepository, times(1)).findByGameAndIsDeletedFalse(anyString());
    }
}
