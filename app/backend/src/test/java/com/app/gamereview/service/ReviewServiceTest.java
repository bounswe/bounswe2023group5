package com.app.gamereview.service;

import com.app.gamereview.dto.request.review.CreateReviewRequestDto;
import com.app.gamereview.dto.request.review.GetAllReviewsFilterRequestDto;
import com.app.gamereview.dto.request.review.UpdateReviewRequestDto;
import com.app.gamereview.dto.response.review.GetAllReviewsResponseDto;
import com.app.gamereview.enums.SortDirection;
import com.app.gamereview.enums.SortType;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.Game;
import com.app.gamereview.model.Profile;
import com.app.gamereview.model.Review;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllReviews() {
        // Arrange
        GetAllReviewsFilterRequestDto filter = new GetAllReviewsFilterRequestDto();
        filter.setGameId("exampleGameId");
        filter.setReviewedBy("exampleUserId");
        filter.setWithDeleted(false);
        filter.setSortBy(SortType.CREATION_DATE.name());
        filter.setSortDirection(SortDirection.DESCENDING.name());

        Review review1 = new Review();
        review1.setId("1");
        review1.setGameId("exampleGameId");
        review1.setReviewedBy("exampleUserId");
        review1.setReviewDescription("Review 1");
        review1.setRating(5);

        Review review2 = new Review();
        review1.setId("2");
        review1.setGameId("exampleGameId");
        review1.setReviewedBy("exampleUserId");
        review1.setReviewDescription("Review 2");
        review1.setRating(4);

        List<Review> mockReviews = new ArrayList<>();
        mockReviews.add(review1);
        mockReviews.add(review2);

        User user = new User();
        user.setId("exampleUserId");
        user.setEmail("user@example.com");

        when(userRepository.findByEmailAndIsDeletedFalse(anyString())).thenReturn(Optional.of(user));
        when(mongoTemplate.find(any(Query.class), eq(Review.class))).thenReturn(new ArrayList<>());

        // Act
        List<GetAllReviewsResponseDto> result = reviewService.getAllReviews(filter, "user@example.com");

        // Assert
        assertNotNull(result);
    }

    @Test
    void testGetReview() {
        Review review = new Review();
        review.setId("existingReviewId");
        review.setReviewedBy("userId");

        User user = new User();
        user.setId("userId");
        user.setUsername("username");
        when(reviewRepository.findById("existingReviewId")).thenReturn(Optional.of(review));
        when(userRepository.findById(review.getReviewedBy())).thenReturn(Optional.of(user));

        GetAllReviewsResponseDto dto = new GetAllReviewsResponseDto();
        dto.setId(review.getId());
        when(modelMapper.map(any(), any())).thenReturn(dto);
        GetAllReviewsResponseDto result = reviewService.getReview("existingReviewId");

        assertNotNull(result);
        assertEquals("existingReviewId", result.getId());
    }

    @Test
    void testGetReviewNotFound() {
        when(reviewRepository.findById("nonExistingReviewId")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.getReview("nonExistingReviewId"));
    }

    @Test
    void testAddReview() {
        CreateReviewRequestDto requestDto = new CreateReviewRequestDto();

        Review review = new Review();
        review.setGameId("gameId");
        review.setRating(5.0f);
        Game game = new Game();
        Profile profile = new Profile();
        profile.setIsReviewedYet(true);

        User user = new User();
        user.setId("userId");
        user.setEmail("user@example.com");

        when(modelMapper.map(any(), any())).thenReturn(review);
        when(mongoTemplate.findOne(any(), any())).thenReturn(profile);
        when(gameRepository.findById(review.getGameId())).thenReturn(Optional.of(game));

        Review result = reviewService.addReview(requestDto, user);

        assertNotNull(result);
        assertEquals("userId", result.getReviewedBy());
    }

    @Test
    void testUpdateReview() {
        UpdateReviewRequestDto requestDto = new UpdateReviewRequestDto();
        requestDto.setRating(4.5f);
        requestDto.setReviewDescription("New Description");

        User user = new User();
        user.setId("userId");
        user.setEmail("user@example.com");

        Review review = new Review();
        review.setId("existingReviewId");
        review.setGameId("gameId");
        review.setReviewedBy("userId");
        review.setReviewDescription("description");
        review.setRating(5.0f);

        Game game = new Game();
        when(reviewRepository.findById("existingReviewId")).thenReturn(Optional.of(review));
        when(gameRepository.findById(review.getGameId())).thenReturn(Optional.of(game));

        UpdateResult expectedUpdateResult = new UpdateResult() {
            @Override
            public boolean wasAcknowledged() {
                return true;
            }

            @Override
            public long getMatchedCount() {
                return 0;
            }

            @Override
            public long getModifiedCount() {
                return 0;
            }

            @Override
            public BsonValue getUpsertedId() {
                return null;
            }
        };
        Query query = new Query(Criteria.where("_id").is(review.getId()));
        Update update = new Update();
        update.set("reviewDescription", requestDto.getReviewDescription());
        update.set("rating", requestDto.getRating());
        when(mongoTemplate.updateFirst(query,update,Review.class)).thenReturn(expectedUpdateResult);
        boolean result = reviewService.updateReview("existingReviewId", requestDto, user);

        assertTrue(result);
    }

    @Test
    void testUpdateReviewNotFound() {
        UpdateReviewRequestDto requestDto = new UpdateReviewRequestDto();
        User user = new User();
        user.setId("userId");
        user.setEmail("user@example.com");
        when(reviewRepository.findById("nonExistingReviewId")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.updateReview("nonExistingReviewId", requestDto, user));
    }

    @Test
    void testDeleteReview() {
        User user = new User();
        user.setId("userId");
        user.setEmail("user@example.com");

        Review review = new Review();
        review.setId("existingReviewId");
        review.setGameId("gameId");
        review.setReviewedBy("userId");
        review.setReviewDescription("description");
        review.setRating(5.0f);

        Profile profile = new Profile();
        profile.setUserId(user.getId());

        Game game = new Game();
        game.setId(review.getGameId());

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(user.getId()));
        when(mongoTemplate.findOne(query, Profile.class)).thenReturn(profile);
        when(reviewRepository.findById("existingReviewId")).thenReturn(Optional.of(review));
        when(gameRepository.findById("gameId")).thenReturn(Optional.of(game));

        boolean result = reviewService.deleteReview("existingReviewId", user);

        assertTrue(result);
    }

    @Test
    void testDeleteReviewNotFound() {

        User user = new User();
        user.setId("userId");
        user.setEmail("user@example.com");

        when(reviewRepository.findById("nonExistingReviewId")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.deleteReview("nonExistingReviewId", user));
    }
}
