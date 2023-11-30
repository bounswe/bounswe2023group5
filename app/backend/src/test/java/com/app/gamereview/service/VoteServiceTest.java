package com.app.gamereview.service;

import com.app.gamereview.dto.request.vote.*;
import com.app.gamereview.enums.*;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.*;
import com.app.gamereview.repository.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private VoteService voteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllVotes() {
        GetAllVotesFilterRequestDto filter = new GetAllVotesFilterRequestDto();
        List<Vote> mockVotes = Arrays.asList(new Vote(), new Vote());
        when(mongoTemplate.find(any(Query.class), eq(Vote.class))).thenReturn(mockVotes);

        List<Vote> result = voteService.getAllVotes(filter);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetVote() {
        Vote existingVote = new Vote();
        existingVote.setId("existingVoteId");
        existingVote.setVotedBy("existingUserId");
        when(voteRepository.findById("existingVoteId")).thenReturn(Optional.of(existingVote));

        Vote result = voteService.getVote("existingVoteId");

        assertNotNull(result);
        assertEquals("existingVoteId", result.getId());
    }

    @Test
    void testAddVote() {
        CreateVoteRequestDto requestDto = new CreateVoteRequestDto();
        requestDto.setChoice(VoteChoice.UPVOTE.name());
        requestDto.setTypeId("someReviewId");
        requestDto.setVoteType(VoteType.REVIEW.name());

        User user = new User();
        user.setId("userId");
        user.setEmail("user@example.com");

        Profile profile = new Profile();
        profile.setUserId("userId");
        profile.setIsVotedYet(true);

        Vote vote = new Vote();
        vote.setVoteType(VoteType.REVIEW);
        vote.setTypeId("someReviewId");
        vote.setChoice(VoteChoice.UPVOTE);
        vote.setVotedBy(user.getId());

        Review review = new Review();

        when(mongoTemplate.findOne(any(Query.class), eq(Profile.class))).thenReturn(profile);
        when(modelMapper.map(any(CreateVoteRequestDto.class), eq(Vote.class))).thenReturn(vote);
        when(reviewRepository.findById(any(String.class))).thenReturn(Optional.of(review));
        when(voteRepository.save(vote)).thenReturn(vote);

        Vote result = voteService.addVote(requestDto, user);

        assertNotNull(result);
        assertEquals("userId", result.getVotedBy());
    }

    @Test
    void testAddVoteResourceNotFound() {
        CreateVoteRequestDto requestDto = new CreateVoteRequestDto();
        requestDto.setTypeId("nonExistingId");
        requestDto.setVoteType(VoteType.REVIEW.name());

        Vote voteToCreate = new Vote();

        User user = new User();
        user.setId("userId");
        user.setEmail("user@example.com");

        when(mongoTemplate.findOne(any(Query.class), eq(Profile.class))).thenReturn(null);
        when(modelMapper.map(requestDto, Vote.class)).thenReturn(voteToCreate);

        assertThrows(ResourceNotFoundException.class, () -> voteService.addVote(requestDto, user));
    }

    @Test
    void testDeleteVoteResourceNotFound() {
        String nonExistingVoteId = "nonExistingVoteId";
        when(voteRepository.findById(nonExistingVoteId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> voteService.deleteVote(nonExistingVoteId));
    }
}
