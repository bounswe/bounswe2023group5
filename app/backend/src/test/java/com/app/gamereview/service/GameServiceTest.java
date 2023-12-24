package com.app.gamereview.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.app.gamereview.dto.request.game.*;
import com.app.gamereview.dto.response.game.GameDetailResponseDto;
import com.app.gamereview.dto.response.game.GetGameListResponseDto;
import com.app.gamereview.dto.response.tag.AddGameTagResponseDto;
import com.app.gamereview.dto.response.tag.GetAllTagsOfGameResponseDto;
import com.app.gamereview.enums.TagType;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.*;
import com.app.gamereview.repository.*;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ForumRepository forumRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GameService gameService;

    @Test
    void testCreateGame() {
        // Arrange
        CreateGameRequestDto requestDto = new CreateGameRequestDto();
        requestDto.setGameName("Test Game");
        requestDto.setDeveloper("developer");

        List<String> artstyles = new ArrayList<>();
        artstyles.add("art");

        List<String> other = new ArrayList<>();
        other.add("other");

        requestDto.setArtStyles(artstyles);
        requestDto.setOtherTags(other);


        List<String> playerTypes = new ArrayList<>();
        playerTypes.add("single");
        requestDto.setPlayerTypes(playerTypes);

        List<String> genres = new ArrayList<>();
        genres.add("mmorpg");
        requestDto.setGenre(genres);

        List<String> platforms = new ArrayList<>();
        platforms.add("ps3");
        requestDto.setPlatforms(platforms);
        // Add other required fields in the requestDto

        Tag developer = new Tag();
        developer.setTagType(TagType.DEVELOPER);

        Tag single = new Tag();
        single.setTagType(TagType.PLAYER_TYPE);

        Tag mmorpg = new Tag();
        mmorpg.setTagType(TagType.GENRE);

        Tag ps3 = new Tag();
        ps3.setTagType(TagType.PLATFORM);

        Tag art = new Tag();
        art.setTagType(TagType.ART_STYLE);

        Tag otherTag = new Tag();
        otherTag.setTagType(TagType.OTHER);

        Game gameToCreate = new Game();
        gameToCreate.setGameName("Test Game");

        // Mock the repository calls
        when(gameRepository.findByGameNameAndIsDeletedFalse("Test Game")).thenReturn(Optional.empty());
        when(tagRepository.findByIdAndIsDeletedFalse(eq("developer"))).thenReturn(Optional.of(developer));
        when(tagRepository.findByIdAndIsDeletedFalse(eq("single"))).thenReturn(Optional.of(single));
        when(tagRepository.findByIdAndIsDeletedFalse(eq("mmorpg"))).thenReturn(Optional.of(mmorpg));
        when(tagRepository.findByIdAndIsDeletedFalse(eq("ps3"))).thenReturn(Optional.of(ps3));
        when(tagRepository.findByIdAndIsDeletedFalse(eq("art"))).thenReturn(Optional.of(art));
        when(tagRepository.findByIdAndIsDeletedFalse(eq("other"))).thenReturn(Optional.of(otherTag));
        when(modelMapper.map(any(CreateGameRequestDto.class), eq(Game.class))).thenReturn(gameToCreate);
        when(gameRepository.save(any(Game.class))).thenReturn(gameToCreate);

        // Act
        Game createdGame = gameService.createGame(requestDto);

        // Assert
        assertNotNull(createdGame);
        assertEquals("Test Game", createdGame.getGameName());

        // Verify repository method calls
        verify(gameRepository, times(1)).findByGameNameAndIsDeletedFalse("Test Game");
        verify(tagRepository, times(6)).findByIdAndIsDeletedFalse(any());
        verify(gameRepository, times(1)).save(any(Game.class));
        verify(forumRepository, times(1)).save(any(Forum.class));
    }

    @Test
    void testGetGameTags() {
        // Arrange
        String gameId = "123";
        List<Tag> artTags = Arrays.asList(
                new Tag("Art1", TagType.ART_STYLE, "blue"),
                new Tag("Art2", TagType.ART_STYLE, "red")
        );

        Tag developer = new Tag("developer", TagType.DEVELOPER, "blue");

        Game game = new Game();
        game.addTag(developer);
        game.addTag(artTags.get(0));
        game.addTag(artTags.get(1));

        // Mock the repository call
        when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(game));

        // Act
        GetAllTagsOfGameResponseDto actualTags = gameService.getGameTags(gameId);

        List<String> artTagIds = new ArrayList<>();

        for (Tag t : artTags) {
            artTagIds.add(t.getId());
        }

        // Assert
        assertEquals(artTagIds, game.getArtStyles());
        assertEquals(developer.getId(), game.getDeveloper());


        assertEquals(actualTags.getPlayerTypes(), new ArrayList<>());
        assertEquals(actualTags.getGenre(), new ArrayList<>());
        assertNull(actualTags.getProduction());
        assertNull(actualTags.getDuration());
        assertEquals(actualTags.getPlatforms(), new ArrayList<>());
        assertEquals(actualTags.getOtherTags(), new ArrayList<>());

        // Verify repository method call
        verify(gameRepository, times(1)).findById(eq(gameId));
    }

    @Test
    void testAddGameTag() {

        AddGameTagRequestDto request = new AddGameTagRequestDto("game", "tag");

        Game game = new Game();

        Tag tag = new Tag();
        tag.setTagType(TagType.DEVELOPER);

        when(gameRepository.findById(eq("game"))).thenReturn(Optional.of(game));
        when(tagRepository.findById(eq("tag"))).thenReturn(Optional.of(tag));

        AddGameTagResponseDto response = gameService.addGameTag(request);

        assertEquals(response.getGameId(), game.getId());
        assertEquals(response.getAddedTag(), tag);

        verify(gameRepository, times(1)).findById(eq("game"));
        verify(gameRepository, times(1)).save(eq(game));
        verify(tagRepository, times(1)).findById(eq("tag"));
    }

    @Test
    void testRemoveGameTag() {

        RemoveGameTagRequestDto request = new RemoveGameTagRequestDto("game", "tag");

        Game game = new Game();

        Tag tag = new Tag();
        tag.setTagType(TagType.DEVELOPER);

        game.addTag(tag);

        when(gameRepository.findById(eq("game"))).thenReturn(Optional.of(game));
        when(tagRepository.findById(eq("tag"))).thenReturn(Optional.of(tag));

        Boolean response = gameService.removeGameTag(request);

        assertTrue(response);

        verify(gameRepository, times(1)).findById(eq("game"));
        verify(gameRepository, times(1)).save(eq(game));
        verify(tagRepository, times(1)).findById(eq("tag"));

    }

    @Test
    void testRecommendationByGameId() {
        // Arrange
        String gameId = "game";
        Game baseGame = new Game(); // Set up your base game
        baseGame.setId(gameId);
        baseGame.setGameName("gameName");
        when(gameRepository.findByIdAndIsDeletedFalse(gameId)).thenReturn(Optional.of(baseGame));

        // Mock the MongoDB template to return a list of similar games
        when(mongoTemplate.find(any(Query.class), eq(Game.class)))
                .thenReturn(Arrays.asList(createGame("Game1"), createGame("Game2")));

        // Mock other dependencies as needed

        // Act
        TreeSet<RecommendGameDto> result = gameService.recommendationByGameId(gameId);

        // Assert
        assertNotNull(result);
        // Add more specific assertions based on your expected behavior

        // Verify that certain methods were called with expected parameters
        verify(gameRepository).findByIdAndIsDeletedFalse(gameId);
        verify(mongoTemplate, times(2)).find(any(Query.class), eq(Game.class));
    }

    @Test
    void testGetGameDetailWhenGameExists() {
        // Arrange
        String gameId = "123";
        Game game = new Game();  // Assuming you have a Game entity with necessary properties
        game.setId(gameId);

        Tag tag = new Tag("tag", TagType.DEVELOPER, "blue");
        game.addTag(tag);

        GameDetailResponseDto expectedResponse = new GameDetailResponseDto();
        expectedResponse.setId(gameId);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(tagRepository.findById(any())).thenReturn(Optional.of(tag)); // Assuming Tag entity is available
        when(modelMapper.map(eq(game), eq(GameDetailResponseDto.class))).thenReturn(expectedResponse);

        // Act
        GameDetailResponseDto actualResponse = gameService.getGameDetail(gameId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(gameRepository, times(1)).findById(gameId);
        verify(tagRepository, times(1)).findById(any());
        verify(modelMapper, times(1)).map(eq(game), eq(GameDetailResponseDto.class));
    }

    @Test
    void testGetGameByNameWhenGameExists() {
        // Arrange
        String gameName = "TestGame";
        Game game = new Game();  // Assuming you have a Game entity with necessary properties
        game.setGameName(gameName);

        Tag tag = new Tag("tag", TagType.DEVELOPER, "blue");
        game.addTag(tag);

        GameDetailResponseDto expectedResponse = new GameDetailResponseDto();
        expectedResponse.setGameName(gameName);

        when(gameRepository.findByGameNameAndIsDeletedFalse(gameName)).thenReturn(Optional.of(game));
        when(tagRepository.findById(any())).thenReturn(Optional.of(tag)); // Assuming Tag entity is available
        when(modelMapper.map(eq(game), eq(GameDetailResponseDto.class))).thenReturn(expectedResponse);

        // Act
        GameDetailResponseDto actualResponse = gameService.getGameByName(gameName);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(gameRepository, times(1)).findByGameNameAndIsDeletedFalse(gameName);
        verify(tagRepository, times(1)).findById(any());
        verify(modelMapper, times(1)).map(eq(game), eq(GameDetailResponseDto.class));
    }

    @Test
    void testGetRecommendedGames() {
        // Arrange
        String userEmail = "test@example.com";
        User user = new User(); // Set up your user

        String gameId = "game";
        Game baseGame = new Game(); // Set up your base game
        baseGame.setId(gameId);
        baseGame.setGameName("gameName");

        Profile profile = new Profile(); // Set up your profile
        profile.addGame(gameId);

        when(userRepository.findByEmailAndIsDeletedFalse(userEmail)).thenReturn(Optional.of(user));

        when(profileRepository.findByUserIdAndIsDeletedFalse(user.getId())).thenReturn(Optional.of(profile));

        when(gameRepository.findByIdAndIsDeletedFalse(gameId)).thenReturn(Optional.of(baseGame));

        // Mock the MongoDB template to return a list of similar games
        when(mongoTemplate.find(any(Query.class), eq(Game.class)))
                .thenReturn(Arrays.asList(createGame("Game1"), createGame("Game2")));


        // Act
        List<Game> result = gameService.getRecommendedGames(userEmail);

        // Assert
        assertNotNull(result);
        // Add more specific assertions based on your expected behavior

        // Verify that certain methods were called with expected parameters
        verify(userRepository).findByEmailAndIsDeletedFalse(userEmail);
        verify(profileRepository).findByUserIdAndIsDeletedFalse(user.getId());
        verify(gameRepository).findByIdAndIsDeletedFalse(gameId);
        verify(mongoTemplate, times(2)).find(any(Query.class), eq(Game.class));
    }

    @Test
    void testGetRecommendedGamesGuest() {
        // Arrange
        List<Game> expectedGames = Arrays.asList(
                createGame("Game1"),
                createGame("Game2"),
                createGame("Game3")
                // Add more games as needed
        );

        when(mongoTemplate.find(any(Query.class), eq(Game.class))).thenReturn(expectedGames);

        // Act
        List<Game> actualGames = gameService.getRecommendedGames();

        // Assert
        assertEquals(expectedGames, actualGames);
    }

    @Test
    void testEditGameWhenGameExists() {
        // Arrange
        String gameId = "123";
        UpdateGameRequestDto updateRequest = new UpdateGameRequestDto();
        updateRequest.setGameName("UpdatedName");
        updateRequest.setGameDescription("UpdatedDescription");
        updateRequest.setGameIcon("UpdatedIcon");
        // Set other properties as needed

        Game existingGame = new Game();
        existingGame.setId(gameId);

        when(gameRepository.findByIdAndIsDeletedFalse(gameId)).thenReturn(Optional.of(existingGame));
        when(gameRepository.save(any())).thenReturn(existingGame);

        // Act
        Game updatedGame = gameService.editGame(gameId, updateRequest);

        // Assert
        assertEquals(updateRequest.getGameName(), updatedGame.getGameName());
        assertEquals(updateRequest.getGameDescription(), updatedGame.getGameDescription());
        assertEquals(updateRequest.getGameIcon(), updatedGame.getGameIcon());
        // Add additional assertions for other properties as needed
    }

    @Test
    void testEditGameWhenGameNotExists() {
        // Arrange
        String gameId = "NonExistentGame";
        UpdateGameRequestDto updateRequest = new UpdateGameRequestDto();

        when(gameRepository.findByIdAndIsDeletedFalse(gameId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> gameService.editGame(gameId, updateRequest));
    }

    @Test
    void testDeleteGameWhenGameExists() {
        // Arrange
        String gameId = "123";
        Game existingGame = new Game();
        existingGame.setId(gameId);

        when(gameRepository.findByIdAndIsDeletedFalse(gameId)).thenReturn(Optional.of(existingGame));
        when(gameRepository.save(any())).thenReturn(existingGame);

        // Act
        boolean result = gameService.deleteGame(gameId);

        // Assert
        assertTrue(result);
        assertTrue(existingGame.getIsDeleted());
    }

    @Test
    void testDeleteGameWhenGameNotExists() {
        // Arrange
        String gameId = "NonExistentGame";
        when(gameRepository.findByIdAndIsDeletedFalse(gameId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> gameService.deleteGame(gameId));
    }

    @Test
    void testChangePromotionStatusOfGameWhenGameExists() {
        // Arrange
        String gameId = "123";
        Game existingGame = new Game();
        existingGame.setId(gameId);
        existingGame.setIsPromoted(false);

        when(gameRepository.findByIdAndIsDeletedFalse(gameId)).thenReturn(Optional.of(existingGame));
        when(gameRepository.save(any())).thenReturn(existingGame);

        // Act
        Game updatedGame = gameService.changePromotionStatusOfGame(gameId);

        // Assert
        assertTrue(updatedGame.getIsPromoted());
        verify(gameRepository, times(1)).save(existingGame);
    }

    @Test
    void testChangePromotionStatusOfGameWhenGameNotExists() {
        // Arrange
        String gameId = "NonExistentGame";
        when(gameRepository.findByIdAndIsDeletedFalse(gameId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> gameService.changePromotionStatusOfGame(gameId));
        verify(gameRepository, never()).save(any());
    }

    @Test
    void testCalculateSimilarityScore() {
        // Arrange
        Game basedGame = new Game();
        basedGame.setId("1");

        List<Tag> mockTags = Arrays.asList(
                createTag("tag1", TagType.PRODUCTION),
                createTag("tag2", TagType.GENRE),
                createTag("tag3", TagType.GENRE),
                createTag("tag4", TagType.PRODUCTION)
        );

        basedGame.addTag(mockTags.get(0));
        basedGame.addTag(mockTags.get(1));
        basedGame.addTag(mockTags.get(2));
        basedGame.addTag(mockTags.get(3));

        Game candidateGame = new Game();
        candidateGame.setId("2");

        candidateGame.addTag(mockTags.get(1));
        candidateGame.addTag(mockTags.get(2));
        candidateGame.addTag(mockTags.get(3));

        when(tagRepository.findByIdAndIsDeletedFalse("tag2")).thenReturn(Optional.of(mockTags.get(1)));
        when(tagRepository.findByIdAndIsDeletedFalse("tag3")).thenReturn(Optional.of(mockTags.get(2)));
        when(tagRepository.findByIdAndIsDeletedFalse("tag4")).thenReturn(Optional.of(mockTags.get(3)));

        // Act
        int similarityScore = gameService.calculateSimilarityScore(basedGame, candidateGame);

        // Assert
        assertEquals(13, similarityScore);
    }

    @Test
    void testGetGames() {
        // Arrange
        GetGameListRequestDto filter = new GetGameListRequestDto();
        filter.setGameName("TestGame");
        filter.setFindDeleted(false);

        List<String> playerTypes = new ArrayList<>();
        playerTypes.add("playerType");

        List<String> genres = new ArrayList<>();
        genres.add("genre");

        List<String> platforms = new ArrayList<>();
        platforms.add("platform");

        List<String> artstyles = new ArrayList<>();
        artstyles.add("art");

        filter.setPlayerTypes(playerTypes);
        filter.setGenre(genres);
        filter.setProduction("aaa");
        filter.setPlatform(platforms);
        filter.setArtStyle(artstyles);

        Game filtered1 = new Game();

        Game filtered2 = new Game();

        List<Game> filteredGames = Arrays.asList(
                filtered1,
                filtered2
        );

        when(mongoTemplate.aggregate(any(Aggregation.class), eq("Game"), eq(Game.class)))
                .thenReturn(new AggregationResults<>(Collections.emptyList(), new Document()));
//        when(mongoTemplate.aggregate(any(Aggregation.class), eq("Game"), eq(Game.class)).getMappedResults()).thenReturn(promotedGames);
        when(mongoTemplate.find(any(Query.class), eq(Game.class))).thenReturn(filteredGames);

        // Act
        List<GetGameListResponseDto> result = gameService.getGames(filter);

        // Assert
        assertEquals(2, result.size()); // 2 promoted games + 2 filtered games
        verify(mongoTemplate, times(1)).aggregate(any(Aggregation.class), eq("Game"), eq(Game.class));
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Game.class));
    }

    @Test
    void testGetAllGames() {
        // Arrange
        GetGameListRequestDto filter = new GetGameListRequestDto();
        filter.setGameName("TestGame");
        filter.setFindDeleted(false);

        List<String> playerTypes = new ArrayList<>();
        playerTypes.add("playerType");

        List<String> genres = new ArrayList<>();
        genres.add("genre");

        List<String> platforms = new ArrayList<>();
        platforms.add("platform");

        List<String> artstyles = new ArrayList<>();
        artstyles.add("art");

        filter.setPlayerTypes(playerTypes);
        filter.setGenre(genres);
        filter.setProduction("aaa");
        filter.setPlatform(platforms);
        filter.setArtStyle(artstyles);

        Game filtered1 = new Game();

        Game filtered2 = new Game();

        List<Game> filteredGames = Arrays.asList(
                filtered1,
                filtered2
        );

        when(mongoTemplate.aggregate(any(Aggregation.class), eq("Game"), eq(Game.class)))
                .thenReturn(new AggregationResults<>(Collections.emptyList(), new Document()));
//        when(mongoTemplate.aggregate(any(Aggregation.class), eq("Game"), eq(Game.class)).getMappedResults()).thenReturn(promotedGames);
        when(mongoTemplate.find(any(Query.class), eq(Game.class))).thenReturn(filteredGames);

        // Act
        List<GetGameListResponseDto> result = gameService.getAllGames(filter);

        // Assert
        assertEquals(2, result.size()); // 2 promoted games + 2 filtered games
        verify(mongoTemplate, times(1)).aggregate(any(Aggregation.class), eq("Game"), eq(Game.class));
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Game.class));
    }

    private Game createGame(String gameName) {
        Game game = new Game();
        game.setGameName(gameName);
        // Set other properties as needed
        return game;
    }

    private Tag createTag(String tagId, TagType type) {
        Tag tag = new Tag();
        tag.setId(tagId);
        tag.setTagType(type);
        // Set other properties as needed
        return tag;
    }
}