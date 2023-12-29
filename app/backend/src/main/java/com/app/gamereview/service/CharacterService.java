package com.app.gamereview.service;

import com.app.gamereview.dto.request.character.UpdateCharacterRequestDto;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.Game;
import com.app.gamereview.model.Character;
import com.app.gamereview.repository.CharacterRepository;
import com.app.gamereview.repository.GameRepository;
import com.app.gamereview.dto.request.character.CreateCharacterRequestDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;

    private final GameRepository gameRepository;

    private final ModelMapper modelMapper;

    public CharacterService(CharacterRepository characterRepository, GameRepository gameRepository, ModelMapper modelMapper) {
        this.characterRepository = characterRepository;
        this.gameRepository = gameRepository;
        this.modelMapper = modelMapper;
    }

    public Character createCharacter(CreateCharacterRequestDto characterRequestDto) {

        List<String> games = characterRequestDto.getGames();


        if (games.isEmpty()) {
            throw new BadRequestException("A character should have at least one game.");
        }

        for (String gameId : games) {
            Optional<Game> gameOptional = gameRepository.findByIdAndIsDeletedFalse(gameId);

            if (gameOptional.isEmpty()) {
                throw new ResourceNotFoundException("Game with the given is not found.");
            }
        }

        Character characterToCreate = modelMapper.map(characterRequestDto, Character.class);

        return characterRepository.save(characterToCreate);
    }

    public Character updateCharacter(String id, UpdateCharacterRequestDto requestDto) {
        Optional<Character> characterOptional = characterRepository.findByIdAndIsDeletedFalse(id);

        if (characterOptional.isEmpty()) {
            throw new ResourceNotFoundException("Character with the given id is not found.");
        }

        Character characterToUpdate = characterOptional.get();

        if (requestDto.getName() != null) {
            if (requestDto.getName().isBlank()) {
                throw new BadRequestException("Character name cannot be blank.");
            }
            characterToUpdate.setName(requestDto.getName());
        }

        if (requestDto.getIcon() != null) {
            if (requestDto.getIcon().isBlank()) {
                throw new BadRequestException("Character icon cannot be blank.");
            }
            characterToUpdate.setIcon(requestDto.getIcon());
        }

        if (requestDto.getDescription() != null) {
            if (requestDto.getDescription().isBlank()) {
                throw new BadRequestException("Character description cannot be blank.");
            }
            characterToUpdate.setDescription(requestDto.getDescription());
        }

        if (requestDto.getGames() != null) {

            if (requestDto.getGames().isEmpty()) {
                throw new BadRequestException("A character should have at least one game.");
            }

            for (String gameId : requestDto.getGames()) {
                Optional<Game> gameOptional = gameRepository.findByIdAndIsDeletedFalse(gameId);

                if (gameOptional.isEmpty()) {
                    throw new ResourceNotFoundException("Game with the given is not found.");
                }
            }

            characterToUpdate.setGames(requestDto.getGames());
        }

        if (requestDto.getType() != null) {
            if (requestDto.getType().isBlank()) {
                characterToUpdate.setType(null);
            } else {
                characterToUpdate.setType(requestDto.getType());
            }
        }

        if (requestDto.getGender() != null) {
            if (requestDto.getGender().isBlank()) {
                characterToUpdate.setGender(null);
            } else {
                characterToUpdate.setGender(requestDto.getGender());
            }
        }

        if (requestDto.getRace() != null) {
            if (requestDto.getRace().isBlank()) {
                characterToUpdate.setRace(null);
            } else {
                characterToUpdate.setRace(requestDto.getRace());
            }
        }

        if (requestDto.getStatus() != null) {
            if (requestDto.getStatus().isBlank()) {
                characterToUpdate.setStatus(null);
            } else {
                characterToUpdate.setStatus(requestDto.getStatus());
            }
        }

        if (requestDto.getOccupation() != null) {
            if (requestDto.getOccupation().isBlank()) {
                characterToUpdate.setOccupation(null);
            } else {
                characterToUpdate.setOccupation(requestDto.getOccupation());
            }
        }

        if (requestDto.getBirthDate() != null) {
            if (requestDto.getBirthDate().isBlank()) {
                characterToUpdate.setBirthDate(null);
            } else {
                characterToUpdate.setBirthDate(requestDto.getBirthDate());
            }
        }

        if (requestDto.getVoiceActor() != null) {
            if (requestDto.getVoiceActor().isBlank()) {
                characterToUpdate.setVoiceActor(null);
            } else {
                characterToUpdate.setVoiceActor(requestDto.getVoiceActor());
            }
        }

        if (requestDto.getHeight() != null) {
            if (requestDto.getHeight().isBlank()) {
                characterToUpdate.setHeight(null);
            } else {
                characterToUpdate.setHeight(requestDto.getHeight());
            }
        }

        if (requestDto.getAge() != null) {
            if (requestDto.getAge().isBlank()) {
                characterToUpdate.setAge(null);
            } else {
                characterToUpdate.setAge(requestDto.getAge());
            }
        }

        if (requestDto.getCustomFields() != null) {
            characterToUpdate.setCustomFields(requestDto.getCustomFields());
        }

        characterRepository.save(characterToUpdate);
        return characterToUpdate;
    }

    public Character deleteCharacter(String id) {
        Optional<Character> characterOptional = characterRepository.findByIdAndIsDeletedFalse(id);

        if (characterOptional.isEmpty()) {
            throw new ResourceNotFoundException("Character with the given id is not found.");
        }

        Character characterToDelete = characterOptional.get();

        characterToDelete.setIsDeleted(true);

        characterRepository.save(characterToDelete);
        return characterToDelete;
    }

    public List<Character> getGameCharacters(String gameId) {

        Optional<Game> gameOptional = gameRepository.findByIdAndIsDeletedFalse(gameId);

        if (gameOptional.isEmpty()) {
            throw new ResourceNotFoundException("Game with the given is not found.");
        }

        return characterRepository.findByGamesContains(gameId);
    }


}
