package com.app.gamereview.service;

import com.app.gamereview.dto.request.notification.CreateNotificationRequestDto;
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
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private NotificationRepository notificationRepository;

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

    @Mock NotificationService notificationService;

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
    void testAddReviewVoteFirstTime() {
        CreateVoteRequestDto requestDto = new CreateVoteRequestDto();
        requestDto.setChoice(VoteChoice.DOWNVOTE.name());
        requestDto.setTypeId("newReviewId");
        requestDto.setVoteType(VoteType.REVIEW.name());

        User user = new User();
        user.setId("userId");
        user.setUsername("userName");

        Profile profile = new Profile();
        profile.setUserId("userId");
        profile.setIsVotedYet(false);

        Vote newVote = new Vote();
        newVote.setId("newVoteId");
        newVote.setChoice(VoteChoice.DOWNVOTE);
        newVote.setVotedBy(user.getId());

        Review newReview = new Review();
        newReview.setId("newReviewId");

        when(mongoTemplate.findOne(any(Query.class), eq(Profile.class))).thenReturn(profile);
        when(modelMapper.map(requestDto, Vote.class)).thenReturn(newVote);
        when(reviewRepository.findById(requestDto.getTypeId())).thenReturn(Optional.of(newReview));
        when(notificationService.createNotification(any(CreateNotificationRequestDto.class))).thenReturn(null);
        when(voteRepository.save(any(Vote.class))).thenReturn(newVote);

        Vote result = voteService.addVote(requestDto, user);

        assertNotNull(result);
        assertEquals(VoteChoice.DOWNVOTE, result.getChoice());
        assertEquals("newVoteId", result.getId());
        assertEquals(user.getId(), result.getVotedBy());
    }

    @Test
    void testAddCommentVoteFirstTime() {
        CreateVoteRequestDto requestDto = new CreateVoteRequestDto();
        requestDto.setChoice(VoteChoice.DOWNVOTE.name());
        requestDto.setTypeId("newCommentId");
        requestDto.setVoteType(VoteType.COMMENT.name());

        User user = new User();
        user.setId("userId");
        user.setUsername("userName");

        Profile profile = new Profile();
        profile.setUserId("userId");
        profile.setIsVotedYet(false);

        Vote newVote = new Vote();
        newVote.setId("newVoteId");
        newVote.setChoice(VoteChoice.DOWNVOTE);
        newVote.setVotedBy(user.getId());

        Comment newComment = new Comment();
        newComment.setId("newCommentId");

        when(mongoTemplate.findOne(any(Query.class), eq(Profile.class))).thenReturn(profile);
        when(modelMapper.map(requestDto, Vote.class)).thenReturn(newVote);
        when(commentRepository.findById(requestDto.getTypeId())).thenReturn(Optional.of(newComment));
        when(notificationService.createNotification(any(CreateNotificationRequestDto.class))).thenReturn(null);
        when(voteRepository.save(any(Vote.class))).thenReturn(newVote);

        Vote result = voteService.addVote(requestDto, user);

        assertNotNull(result);
        assertEquals(VoteChoice.DOWNVOTE, result.getChoice());
        assertEquals("newVoteId", result.getId());
        assertEquals(user.getId(), result.getVotedBy());
    }


    @Test
    void testAddPostVoteFirstTime() {
        CreateVoteRequestDto requestDto = new CreateVoteRequestDto();
        requestDto.setChoice(VoteChoice.DOWNVOTE.name());
        requestDto.setTypeId("newPostId");
        requestDto.setVoteType(VoteType.POST.name());

        User user = new User();
        user.setId("userId");
        user.setUsername("userName");

        Profile profile = new Profile();
        profile.setUserId("userId");
        profile.setIsVotedYet(false);

        Vote newVote = new Vote();
        newVote.setId("newVoteId");
        newVote.setChoice(VoteChoice.DOWNVOTE);
        newVote.setVotedBy(user.getId());

        Post newPost = new Post();
        newPost.setId("newPostId");

        Notification newNotification = new Notification();

        when(mongoTemplate.findOne(any(Query.class), eq(Profile.class))).thenReturn(profile);
        when(modelMapper.map(requestDto, Vote.class)).thenReturn(newVote);
        when(postRepository.findById(requestDto.getTypeId())).thenReturn(Optional.of(newPost));
        when(notificationService.createNotification(any(CreateNotificationRequestDto.class))).thenReturn(null);
        when(notificationRepository.findByParentAndUser(any(String.class), any(String.class))).thenReturn(Optional.of(newNotification));
        when(voteRepository.save(any(Vote.class))).thenReturn(newVote);

        Vote result = voteService.addVote(requestDto, user);

        assertNotNull(result);
        assertEquals(VoteChoice.DOWNVOTE, result.getChoice());
        assertEquals("newVoteId", result.getId());
        assertEquals(user.getId(), result.getVotedBy());
    }

    @Test
    void testAddPostVoteFirstTimeWithOverallVote10() {
        CreateVoteRequestDto requestDto = new CreateVoteRequestDto();
        requestDto.setChoice(VoteChoice.DOWNVOTE.name());
        requestDto.setTypeId("newPostId");
        requestDto.setVoteType(VoteType.POST.name());

        User user = new User();
        user.setId("userId");
        user.setUsername("userName");

        Profile profile = new Profile();
        profile.setUserId("userId");
        profile.setIsVotedYet(false);

        Vote newVote = new Vote();
        newVote.setId("newVoteId");
        newVote.setChoice(VoteChoice.DOWNVOTE);
        newVote.setVotedBy(user.getId());

        Post newPost = new Post();
        newPost.setId("newPostId");
        newPost.setOverallVote(11);     // plus 1 since downovote
        newPost.setPoster("userId");
        newPost.setTitle("Test Post");

        Notification newNotification = new Notification();

        when(userRepository.findById(any(String.class))).thenReturn(Optional.of(user));
        when(mongoTemplate.findOne(any(Query.class), eq(Profile.class))).thenReturn(profile);
        when(modelMapper.map(requestDto, Vote.class)).thenReturn(newVote);
        when(postRepository.findById(requestDto.getTypeId())).thenReturn(Optional.of(newPost));
        when(notificationService.createNotification(any(CreateNotificationRequestDto.class))).thenReturn(null);
        when(notificationRepository.findByParentAndUser(any(String.class), any(String.class))).thenReturn(Optional.of(newNotification));
        when(voteRepository.save(any(Vote.class))).thenReturn(newVote);

        Vote result = voteService.addVote(requestDto, user);

        assertNotNull(result);
        assertEquals(VoteChoice.DOWNVOTE, result.getChoice());
        assertEquals("newVoteId", result.getId());
        assertEquals(user.getId(), result.getVotedBy());
    }


    @Test
    void testAddPostVoteFirstTimeWithOverallVote10NoParent() {
        CreateVoteRequestDto requestDto = new CreateVoteRequestDto();
        requestDto.setChoice(VoteChoice.DOWNVOTE.name());
        requestDto.setTypeId("newPostId");
        requestDto.setVoteType(VoteType.POST.name());

        User user = new User();
        user.setId("userId");
        user.setUsername("userName");

        Profile profile = new Profile();
        profile.setUserId("userId");
        profile.setIsVotedYet(false);

        Vote newVote = new Vote();
        newVote.setId("newVoteId");
        newVote.setChoice(VoteChoice.DOWNVOTE);
        newVote.setVotedBy(user.getId());

        Post newPost = new Post();
        newPost.setId("newPostId");
        newPost.setOverallVote(11);     // plus 1 since downovote
        newPost.setPoster("userId");
        newPost.setTitle("Test Post");

        Notification newNotification = new Notification();

        when(userRepository.findById(any(String.class))).thenReturn(Optional.of(user));
        when(mongoTemplate.findOne(any(Query.class), eq(Profile.class))).thenReturn(profile);
        when(modelMapper.map(requestDto, Vote.class)).thenReturn(newVote);
        when(postRepository.findById(requestDto.getTypeId())).thenReturn(Optional.of(newPost));
        when(notificationService.createNotification(any(CreateNotificationRequestDto.class))).thenReturn(null);
        when(notificationRepository.findByParentAndUser(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(voteRepository.save(any(Vote.class))).thenReturn(newVote);

        Vote result = voteService.addVote(requestDto, user);

        assertNotNull(result);
        assertEquals(VoteChoice.DOWNVOTE, result.getChoice());
        assertEquals("newVoteId", result.getId());
        assertEquals(user.getId(), result.getVotedBy());
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

    @Test
    void testDeleteVote() {

        Vote vote = new Vote();
        vote.setId("existingVoteId");

        String existingVoteId = "existingVoteId";
        when(voteRepository.findById(existingVoteId)).thenReturn(Optional.of(vote));

        Boolean result = voteService.deleteVote(vote.getId());

        assertNotNull(result);
        assertEquals(true,result);
    }
}
