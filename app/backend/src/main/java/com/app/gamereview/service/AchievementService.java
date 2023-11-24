package com.app.gamereview.service;

import com.app.gamereview.dto.request.achievement.CreateAchievementRequestDto;
import com.app.gamereview.dto.request.achievement.GrantAchievementRequestDto;
import com.app.gamereview.dto.request.achievement.UpdateAchievementRequestDto;
import com.app.gamereview.enums.AchievementType;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.Achievement;
import com.app.gamereview.model.Game;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.AchievementRepository;
import com.app.gamereview.repository.GameRepository;
import com.app.gamereview.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;

    private final GameRepository gameRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public AchievementService(AchievementRepository achievementRepository, GameRepository gameRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.achievementRepository = achievementRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public Achievement createAchievement(CreateAchievementRequestDto achievementRequestDto) {

        if (achievementRequestDto.getType().equals(AchievementType.GAME.toString())) {

            String gameId = achievementRequestDto.getGame();

            if (gameId == null) {
                throw new BadRequestException("Game id cannot be empty for game achievements.");
            }

            Optional<Game> gameOptional = gameRepository.findByIdAndIsDeletedFalse(gameId);

            if (gameOptional.isEmpty()) {
                throw new ResourceNotFoundException("Game with the given is not found.");
            }
        } else {
            achievementRequestDto.setGame("-1");
        }

        List<Achievement> achievementsSameName = achievementRepository.findByTitleAndIsDeletedFalse(achievementRequestDto.getTitle());

        for (Achievement achievement : achievementsSameName) {
            if (achievement.getGame().equals(achievementRequestDto.getGame())) {
                throw new BadRequestException("There is already an achievement with the same name.");
            }
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

        if (requestDto.getIcon() != null) {
            achievementToUpdate.setIcon(requestDto.getIcon());
        }

        if (requestDto.getDescription() != null) {
            achievementToUpdate.setDescription(requestDto.getDescription());
        }

        if (requestDto.getTitle() != null) {

            List<Achievement> achievementsSameName = achievementRepository.findByTitleAndIsDeletedFalse(requestDto.getTitle());

            for (Achievement achievement : achievementsSameName) {
                if (achievement.getGame().equals(achievementToUpdate.getGame()) &&
                        !achievement.getId().equals(achievementToUpdate.getId())) {
                    throw new BadRequestException("There is already an achievement with the same name.");
                }
            }

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

    public List<String> grantAchievement(GrantAchievementRequestDto request) {
        Optional<Achievement> achievementOptional = achievementRepository.findByIdAndIsDeletedFalse(request.getAchievementId());

        if (achievementOptional.isEmpty()) {
            throw new ResourceNotFoundException("Achievement with the given id is not found.");
        }

        Optional<User> userOptional = userRepository.findByIdAndIsDeletedFalse(request.getUserId());

        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User with the given id is not found.");
        }

        User userToGrant = userOptional.get();

        List<String> userAchievements = userToGrant.getAchievements();

        if (userAchievements.contains(request.getAchievementId())) {
            throw new BadRequestException("User already has the given achievement.");
        }

        userToGrant.getAchievements().add(request.getAchievementId());

        userRepository.save(userToGrant);
        return userToGrant.getAchievements();
    }

    public List<Achievement> getGameAchievements(String gameId) {

        Optional<Game> gameOptional = gameRepository.findByIdAndIsDeletedFalse(gameId);

        if (gameOptional.isEmpty()) {
            throw new ResourceNotFoundException("Game with the given is not found.");
        }

        return achievementRepository.findByGameAndIsDeletedFalse(gameId);
    }
}
