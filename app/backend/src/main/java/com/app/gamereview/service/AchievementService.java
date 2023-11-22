package com.app.gamereview.service;

import com.app.gamereview.dto.request.achievement.CreateAchievementRequestDto;
import com.app.gamereview.dto.request.achievement.UpdateAchievementRequestDto;
import com.app.gamereview.enums.AchievementType;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.Achievement;
import com.app.gamereview.model.Game;
import com.app.gamereview.repository.AchievementRepository;
import com.app.gamereview.repository.GameRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;

    private final GameRepository gameRepository;

    private final ModelMapper modelMapper;

    public AchievementService(AchievementRepository achievementRepository, GameRepository gameRepository, ModelMapper modelMapper) {
        this.achievementRepository = achievementRepository;
        this.gameRepository = gameRepository;
        this.modelMapper = modelMapper;
    }

    public Achievement createAchievement(CreateAchievementRequestDto achievementRequestDto) {

        Optional<Achievement> achievementOptional = achievementRepository.findByTitleAndIsDeletedFalse(achievementRequestDto.getTitle());

        if (achievementOptional.isPresent() && Objects.equals(achievementOptional.get().getGameId(), achievementRequestDto.getGameId())) {
            throw new BadRequestException("There is already an achievement with the same name.");
        }

        if (achievementRequestDto.getType() == AchievementType.GAME) {

            String gameId = achievementRequestDto.getGameId();

            if (gameId.isEmpty()) {
                throw new BadRequestException("Game id cannot be empty for game achievements.");
            }

            Optional<Game> gameOptional = gameRepository.findByIdAndIsDeletedFalse(gameId);

            if (gameOptional.isEmpty()) {
                throw new ResourceNotFoundException("Game with the given is not found.");
            }
        } else {
            achievementRequestDto.setGameId("-1");
        }

        Achievement achievement = modelMapper.map(achievementRequestDto, Achievement.class);

        return achievementRepository.save(achievement);
    }

    public Achievement updateAchievement(String id, UpdateAchievementRequestDto requestDto) {
        Optional<Achievement> achievementOptional = achievementRepository.findByIdAndIsDeletedFalse(id);

        if (achievementOptional.isEmpty()) {
            throw new ResourceNotFoundException("Achievement with the given id is not found.");
        }

        Achievement achievementToUpdate = achievementOptional.get();

        if (!requestDto.getIcon().isEmpty()) {
            achievementToUpdate.setIcon(requestDto.getIcon());
        }

        if (!requestDto.getDescription().isEmpty()) {
            achievementToUpdate.setDescription(requestDto.getDescription());
        }

        if (!requestDto.getTitle().isEmpty()) {
            achievementToUpdate.setTitle(requestDto.getTitle());
        }

        achievementRepository.save(achievementToUpdate);
        return achievementToUpdate;
    }

    public Achievement deleteAchievement(String id) {
        Optional<Achievement> achievementOptional = achievementRepository.findByIdAndIsDeletedFalse(id);

        if (achievementOptional.isEmpty()) {
            throw new ResourceNotFoundException("Achievement with the given id is not found.");
        }

        Achievement achievementToDelete = achievementOptional.get();

        achievementToDelete.setIsDeleted(true);

        achievementRepository.save(achievementToDelete);
        return achievementToDelete;
    }
}
