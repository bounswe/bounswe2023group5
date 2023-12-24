package com.app.gamereview.service;

import com.app.gamereview.dto.request.character.CreateCharacterRequestDto;
import com.app.gamereview.dto.request.character.UpdateCharacterRequestDto;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.Character;
import com.app.gamereview.model.Game;
import com.app.gamereview.repository.CharacterRepository;
import com.app.gamereview.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CharacterService characterService;

    @BeforeEach
    void setUp() {
        // Set up any common mocking behavior here
    }

    @Test
    void testCreateCharacterWithValidData() {
        // Arrange
        CreateCharacterRequestDto requestDto = new CreateCharacterRequestDto();
        requestDto.setGames(Collections.singletonList("validGameId"));
        Character character = new Character();
        when(modelMapper.map(requestDto, Character.class)).thenReturn(character);
        when(gameRepository.findByIdAndIsDeletedFalse("validGameId")).thenReturn(Optional.of(new Game()));
        when(characterRepository.save(any(Character.class))).thenReturn(character);

        // Act
        Character createdCharacter = characterService.createCharacter(requestDto);

        // Assert
        assertNotNull(createdCharacter, "Character should not be null");
    }

    @Test
    void testCreateCharacterWithEmptyGames() {
        // Arrange
        CreateCharacterRequestDto requestDto = new CreateCharacterRequestDto();
        requestDto.setGames(Collections.emptyList());

        // Act & Assert
        assertThrows(BadRequestException.class,
                () -> characterService.createCharacter(requestDto),
                "Expected BadRequestException to be thrown when games list is empty"
        );
    }

    @Test
    void testCreateCharacterWithNonExistentGame() {
        // Arrange
        CreateCharacterRequestDto requestDto = new CreateCharacterRequestDto();
        requestDto.setGames(Collections.singletonList("nonExistentGameId"));
        when(gameRepository.findByIdAndIsDeletedFalse("nonExistentGameId")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> characterService.createCharacter(requestDto),
                "Expected ResourceNotFoundException to be thrown for a non-existent game"
        );
    }

    @Test
    void testCreateCharacterWithNullRequest() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> characterService.createCharacter(null),
                "Expected NullPointerException to be thrown when request is null"
        );
    }

    @Test
    void testUpdateCharacterWithValidData() {
        // Arrange
        UpdateCharacterRequestDto requestDto = new UpdateCharacterRequestDto();
        requestDto.setName("UpdatedName");
        requestDto.setIcon("UpdatedIcon");
        requestDto.setDescription("UpdatedDescription");
        requestDto.setGames(Collections.singletonList("validGameId"));
        requestDto.setType("UpdatedType");
        requestDto.setGender("UpdatedGender");
        requestDto.setRace("UpdatedRace");
        requestDto.setStatus("UpdatedStatus");
        requestDto.setOccupation("UpdatedOccupation");
        requestDto.setBirthDate("UpdatedBirthDate");
        requestDto.setVoiceActor("UpdatedVoiceActor");
        requestDto.setHeight("UpdatedHeight");
        requestDto.setAge("UpdatedAge");
        requestDto.setCustomFields(Collections.singletonMap("key", "value"));

        Character existingCharacter = new Character();
        when(characterRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(existingCharacter));
        when(gameRepository.findByIdAndIsDeletedFalse("validGameId")).thenReturn(Optional.of(new Game()));
        when(characterRepository.save(any(Character.class))).thenReturn(existingCharacter);

        // Act
        Character updatedCharacter = characterService.updateCharacter("existingId", requestDto);

        // Assert
        assertNotNull(updatedCharacter, "Character should be updated and not null");
        assertEquals("UpdatedName", updatedCharacter.getName());
    }

    @Test
    void testUpdateCharacterWithValidBlankData() {
        // Arrange
        UpdateCharacterRequestDto requestDto = new UpdateCharacterRequestDto();
        requestDto.setName("UpdatedName");
        requestDto.setIcon("UpdatedIcon");
        requestDto.setDescription("description");
        requestDto.setGames(Collections.singletonList("validGameId"));
        requestDto.setType("");
        requestDto.setGender("");
        requestDto.setRace("");
        requestDto.setStatus("");
        requestDto.setOccupation("");
        requestDto.setBirthDate("");
        requestDto.setVoiceActor("");
        requestDto.setHeight("");
        requestDto.setAge("");
        requestDto.setCustomFields(Collections.singletonMap("key", "value"));

        Character existingCharacter = new Character();
        when(characterRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(existingCharacter));
        when(gameRepository.findByIdAndIsDeletedFalse("validGameId")).thenReturn(Optional.of(new Game()));
        when(characterRepository.save(any(Character.class))).thenReturn(existingCharacter);

        // Act
        Character updatedCharacter = characterService.updateCharacter("existingId", requestDto);

        // Assert
        assertNotNull(updatedCharacter, "Character should be updated and not null");
        assertEquals("UpdatedName", updatedCharacter.getName());
    }

    @Test
    void testUpdateCharacterNonExistentCharacter() {
        // Arrange
        when(characterRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> characterService.updateCharacter("nonExistentId", new UpdateCharacterRequestDto()),
                "Expected ResourceNotFoundException for non-existent character ID"
        );
    }

    @Test
    void testUpdateCharacterWithBlankName() {
        // Arrange
        UpdateCharacterRequestDto requestDto = new UpdateCharacterRequestDto();
        requestDto.setName("");

        Character existingCharacter = new Character();
        when(characterRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(existingCharacter));

        // Act & Assert
        assertThrows(BadRequestException.class,
                () -> characterService.updateCharacter("existingId", requestDto),
                "Expected BadRequestException when the name is blank"
        );
    }

    @Test
    void testUpdateCharacterWithBlankIcon() {
        // Arrange
        UpdateCharacterRequestDto requestDto = new UpdateCharacterRequestDto();
        requestDto.setIcon("");

        Character existingCharacter = new Character();
        when(characterRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(existingCharacter));

        // Act & Assert
        assertThrows(BadRequestException.class,
                () -> characterService.updateCharacter("existingId", requestDto),
                "Expected BadRequestException when the icon is blank"
        );
    }

    @Test
    void testUpdateCharacterWithBlankDescription() {
        // Arrange
        UpdateCharacterRequestDto requestDto = new UpdateCharacterRequestDto();
        requestDto.setDescription("");

        Character existingCharacter = new Character();
        when(characterRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(existingCharacter));

        // Act & Assert
        assertThrows(BadRequestException.class,
                () -> characterService.updateCharacter("existingId", requestDto),
                "Expected BadRequestException when the description is blank"
        );
    }

    @Test
    void testUpdateCharacterWithEmptyGamesList() {
        // Arrange
        UpdateCharacterRequestDto requestDto = new UpdateCharacterRequestDto();
        requestDto.setGames(Collections.emptyList());

        Character existingCharacter = new Character();
        when(characterRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(existingCharacter));

        // Act & Assert
        assertThrows(BadRequestException.class,
                () -> characterService.updateCharacter("existingId", requestDto),
                "Expected BadRequestException when the games list is empty"
        );
    }

    @Test
    void testUpdateCharacterWithNonExistentGame() {
        // Arrange
        UpdateCharacterRequestDto requestDto = new UpdateCharacterRequestDto();
        requestDto.setGames(Collections.singletonList("nonExistentGameId"));

        Character existingCharacter = new Character();
        when(characterRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(existingCharacter));
        when(gameRepository.findByIdAndIsDeletedFalse("nonExistentGameId")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> characterService.updateCharacter("existingId", requestDto),
                "Expected ResourceNotFoundException for non-existent game in the games list"
        );
    }

    @Test
    void testUpdateCharacterWithNullRequest() {
        // Arrange
        Character existingCharacter = new Character();
        when(characterRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(existingCharacter));

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> characterService.updateCharacter("existingId", null),
                "Expected NullPointerException when request is null"
        );
    }

    @Test
    void testDeleteCharacterWithValidId() {
        // Arrange
        String validId = "validId";
        Character existingCharacter = new Character();
        when(characterRepository.findByIdAndIsDeletedFalse(validId)).thenReturn(Optional.of(existingCharacter));

        // Act
        Character deletedCharacter = characterService.deleteCharacter(validId);

        // Assert
        assertTrue(deletedCharacter.getIsDeleted(), "Character should be marked as deleted");
        verify(characterRepository).save(deletedCharacter);
    }

    @Test
    void testDeleteNonExistentCharacter() {
        // Arrange
        when(characterRepository.findByIdAndIsDeletedFalse("nonExistentId")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> characterService.deleteCharacter("nonExistentId"),
                "Expected ResourceNotFoundException for a non-existent character ID"
        );
    }

    @Test
    void testDeleteCharacterAlreadyDeleted() {
        // Arrange
        String validId = "validId";
        Character alreadyDeletedCharacter = new Character();
        alreadyDeletedCharacter.setIsDeleted(true);
        // create empty optional character object
        Optional<Character> characterOptional = Optional.empty();
        when(characterRepository.findByIdAndIsDeletedFalse(validId)).thenReturn(characterOptional);

        // Act
        assertThrows(ResourceNotFoundException.class,
                () -> characterService.deleteCharacter(validId),
                "Expected ResourceNotFoundException for a non-existent character ID"
        );

        // Assert
        assertTrue(alreadyDeletedCharacter.getIsDeleted(), "Character should remain deleted");
        verify(characterRepository, never()).save(alreadyDeletedCharacter);
    }

    @Test
    void testGetGameCharactersWithValidGameId() {
        // Arrange
        String validGameId = "validGameId";
        when(gameRepository.findByIdAndIsDeletedFalse(validGameId)).thenReturn(Optional.of(new Game()));
        when(characterRepository.findByGamesContains(validGameId)).thenReturn(Collections.singletonList(new Character()));

        // Act
        List<Character> characters = characterService.getGameCharacters(validGameId);

        // Assert
        assertFalse(characters.isEmpty(), "Character list should not be empty");
    }

    @Test
    void testGetGameCharactersWithNonExistentGameId() {
        // Arrange
        when(gameRepository.findByIdAndIsDeletedFalse("nonExistentGameId")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> characterService.getGameCharacters("nonExistentGameId"),
                "Expected ResourceNotFoundException for a non-existent game ID"
        );
    }
}