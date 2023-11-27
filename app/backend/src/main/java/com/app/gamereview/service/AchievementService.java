package com.app.gamereview.service;

import com.app.gamereview.dto.request.achievement.CreateAchievementRequestDto;
import com.app.gamereview.dto.request.achievement.GrantAchievementRequestDto;
import com.app.gamereview.dto.request.achievement.UpdateAchievementRequestDto;
import com.app.gamereview.enums.AchievementType;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.Achievement;
import com.app.gamereview.model.Game;
import com.app.gamereview.model.Profile;
import com.app.gamereview.repository.AchievementRepository;
import com.app.gamereview.repository.GameRepository;
import com.app.gamereview.repository.ProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;

    private final GameRepository gameRepository;

    private final ProfileRepository profileRepository;

    private final ModelMapper modelMapper;

    public AchievementService(AchievementRepository achievementRepository, GameRepository gameRepository, ProfileRepository profileRepository, ModelMapper modelMapper) {
        this.achievementRepository = achievementRepository;
        this.gameRepository = gameRepository;
        this.profileRepository = profileRepository;
        this.modelMapper = modelMapper;
    }

    public Achievement createAchievement(CreateAchievementRequestDto achievementRequestDto) {

        String gameId = achievementRequestDto.getGame();

        if (achievementRequestDto.getType().equals(AchievementType.GAME.toString())) {

            if (gameId == null) {
                throw new BadRequestException("Game id cannot be empty for game achievements.");
            }

            Optional<Game> gameOptional = gameRepository.findByIdAndIsDeletedFalse(gameId);

            if (gameOptional.isEmpty()) {
                throw new ResourceNotFoundException("Game with the given is not found.");
            }
        } else {
            if (gameId != null) {
                throw new BadRequestException("Meta achievements cannot be linked to any game.");
            }
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

    public Achievement deleteAchievement(String achievementName, String gameName) {
        List<Achievement> achievementWithName = achievementRepository.findByTitleAndIsDeletedFalse(achievementName);

        if (achievementWithName.isEmpty()) {
            throw new ResourceNotFoundException("Achievement with the given name is not found.");
        }

        Achievement achievementToDelete = null;

        if(gameName != null){
            Optional<Game> gameWithName = gameRepository.findByGameNameAndIsDeletedFalse(gameName);

            if (gameWithName.isEmpty()) {
                throw new ResourceNotFoundException("Game with the given name is not found.");
            }

            for (Achievement achievement : achievementWithName) {
                Optional<Game> game = gameRepository.findByIdAndIsDeletedFalse(achievement.getGame());
                if (game.isPresent() && game.get().getGameName().equals(gameName)) {
                    achievementToDelete = achievement;
                }
            }

            if (achievementToDelete == null) {
                throw new ResourceNotFoundException("There is no achievement with the given name in the game.");
            }

        }
        else{
            Optional<Achievement> achievement = achievementRepository.findByTitle(achievementName);

            if(achievement.isEmpty() || achievement.get().getIsDeleted()){
                throw new ResourceNotFoundException("Meta achievement with the given name is not found");
            }

            if(achievement.get().getType() == AchievementType.GAME){
                throw new BadRequestException("Cannot delete game achievement without specifying the game");
            }

            achievementToDelete = achievement.get();

        }

        achievementToDelete.setIsDeleted(true);


        achievementRepository.save(achievementToDelete);
        return achievementToDelete;
    }

    public List<String> grantAchievement(GrantAchievementRequestDto request) {
        Optional<Achievement> achievementOptional = achievementRepository.findByIdAndIsDeletedFalse(request.getAchievementId());

        if (achievementOptional.isEmpty()) {
            throw new ResourceNotFoundException("Achievement with the given id is not found.");
        }

        Optional<Profile> profileOptional = profileRepository.findByUserIdAndIsDeletedFalse(request.getUserId());

        if (profileOptional.isEmpty()) {
            throw new ResourceNotFoundException("Profile of the given user is not found.");
        }

        Profile profileToGrant = profileOptional.get();

        List<String> userAchievements = profileToGrant.getAchievements();

        if (userAchievements.contains(request.getAchievementId())) {
            throw new BadRequestException("User already has the given achievement.");
        }

        profileToGrant.getAchievements().add(request.getAchievementId());

        profileRepository.save(profileToGrant);
        return profileToGrant.getAchievements();
    }

    public List<Achievement> getGameAchievements(String gameId) {

        Optional<Game> gameOptional = gameRepository.findByIdAndIsDeletedFalse(gameId);

        if (gameOptional.isEmpty()) {
            throw new ResourceNotFoundException("Game with the given is not found.");
        }

        return achievementRepository.findByGameAndIsDeletedFalse(gameId);
    }
}
