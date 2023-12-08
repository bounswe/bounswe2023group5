package com.app.gamereview.service;

import com.app.gamereview.dto.response.tag.GetAllTagsOfGameResponseDto;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.Game;
import com.app.gamereview.repository.GameRepository;
import com.app.gamereview.repository.TagRepository;
import com.app.gamereview.service.GameService;
import com.app.gamereview.service.TagService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagService tagService;

    @InjectMocks
    private GameService gameService;
            
    @Mock
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    // @Test
    // void testGetAllGames() {
    //     // Arrange
    //     GetGameListRequestDto filter = new GetGameListRequestDto();
    //     Game game1 = new Game("Game 1", "Description 1", null, "SystemReq 1");
    //     Game game2 = new Game("Game 2", "Description 2", null, "SystemReq 2");
    //     List<Game> games = List.of(game1, game2);
    //     when(gameRepository.findAll()).thenReturn(games);

    //     // Act
    //     List<GetGameListResponseDto> response = gameService.getAllGames(filter);

    //     // Assert
    //     assertEquals(2, response.size());
    //     assertEquals("Game 1", response.get(0).getGameName());
    //     assertEquals("Game 2", response.get(1).getGameName());
    // }
/*
    @Test
    void testGetGameTags() {
        // Arrange
        String gameId = "123";
        Game game = new Game("Game 1", "Description 1", null, "SystemReq 1");
        game.setIsDeleted(false);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        // Act
        GetAllTagsOfGameResponseDto response = gameService.getGameTags(gameId);

        // Assert
        assertNotNull(response);
        assertEquals(game.getPlayerTypes(), response.getPlayerTypes());
        assertEquals(game.getGenre(), response.getGenre());
        assertEquals(game.getProduction(), response.getProduction());
        assertEquals(game.getDuration(), response.getDuration());
        assertEquals(game.getPlatforms(), response.getPlatforms());
        assertEquals(game.getArtStyles(), response.getArtStyles());
        assertEquals(game.getDeveloper(), response.getDeveloper());
        assertEquals(game.getOtherTags(), response.getOtherTags());
    }

    // @Test
    // void testAddGameTag() {
    //     // Arrange
    //     String gameId = "123";
    //     String tagId = "456";
    //     Game game = new Game("Game 1", "Description 1", null, "SystemReq 1");
    //     Tag tag = new Tag("Tag 1", com.app.gamereview.enums.TagType.PLAYER_TYPE, "#c06123");
    //     game.setIsDeleted(false);
    //     tag.setIsDeleted(false);
    //     tag.setId(tagId);
    //     AddGameTagRequestDto request = new AddGameTagRequestDto(gameId, tagId);
    //     when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
    //     when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));
    //     CreateTagRequestDto createRequest = new CreateTagRequestDto("Tag 1", com.app.gamereview.enums.TagType.PLAYER_TYPE, "#c06123");

    //     tagService.createTag(createRequest);
    //     // Act
    //     AddGameTagResponseDto response = gameService.addGameTag(request);

    //     // Assert
    //     assertNotNull(response);
    //     assertEquals(gameId, response.getGameId());
    //     assertEquals(tag, response.getAddedTag());
    //     assertTrue(game.getPlayerTypes().contains(tag));
    // }

    @Test
    void testGetGameDetail() {
        // Arrange
        String gameId = "123";
        Game game = new Game("Game 1", "Description 1", null, "SystemReq 1");
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        // Act
        var response = gameService.getGameDetail(gameId);

        // Assert
        assertNotNull(response);
        //assertEquals(game, response.getGame());
    }

    @Test
    void testGetGameDetailNotFound() {
        // Arrange
        String gameId = "123";
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> gameService.getGameDetail(gameId));
    }
    */

}
