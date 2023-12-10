package com.app.gamereview.service;

import com.app.gamereview.dto.request.comment.CreateCommentRequestDto;
import com.app.gamereview.dto.request.comment.EditCommentRequestDto;
import com.app.gamereview.dto.request.comment.ReplyCommentRequestDto;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.Comment;
import com.app.gamereview.model.Post;
import com.app.gamereview.model.Profile;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTests {

    @Mock
    private PostRepository postRepository;
    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentService commentService;
    @Mock
    private NotificationService notificationService;

    private User user;
    private Post post;
    private Profile profile;
    private Comment comment;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("user123");
        user.setUsername("testuser");

        post = new Post();
        post.setId("post123");
        post.setTitle("Test Post");

        profile = new Profile();
        profile.setUserId("user123");

        comment = new Comment();
        comment.setCommentContent("Test Comment");
        comment.setCommenter(user.getId()); // Set the commenter ID
    }

    @Test
    void createCommentTest() {
        CreateCommentRequestDto request = new CreateCommentRequestDto();
        request.setPost(post.getId());

        when(postRepository.findById(request.getPost())).thenReturn(Optional.of(post));
        when(mongoTemplate.findOne(any(), eq(Profile.class))).thenReturn(profile);

        Comment commentMock = new Comment();
        commentMock.setCommentContent("Test Comment");
        when(modelMapper.map(any(CreateCommentRequestDto.class), eq(Comment.class))).thenReturn(commentMock);

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment createdComment = commentService.createComment(request, user);

        assertNotNull(createdComment);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void createCommentPostNotFoundTest() {
        CreateCommentRequestDto request = new CreateCommentRequestDto();
        request.setPost("invalidPostId");

        when(postRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.createComment(request, user);
        });
    }


    @Test
    void replyCommentTest() {
        ReplyCommentRequestDto request = new ReplyCommentRequestDto();
        request.setParentComment(comment.getId());

        when(commentRepository.findById(request.getParentComment())).thenReturn(Optional.of(comment));
        when(mongoTemplate.findOne(any(), eq(Profile.class))).thenReturn(profile);

        Comment replyCommentMock = new Comment();
        replyCommentMock.setCommentContent("Reply Comment");
        when(modelMapper.map(any(ReplyCommentRequestDto.class), eq(Comment.class))).thenReturn(replyCommentMock);

        when(commentRepository.save(any(Comment.class))).thenReturn(replyCommentMock);

        Comment repliedComment = commentService.replyComment(request, user);

        assertNotNull(repliedComment);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void editCommentTest() {
        String commentId = comment.getId();
        EditCommentRequestDto request = new EditCommentRequestDto();
        request.setCommentContent("Updated Comment");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment editedComment = commentService.editComment(commentId, request, user);

        assertNotNull(editedComment);
        assertEquals("Updated Comment", editedComment.getCommentContent());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void deleteCommentTest() {
        String commentId = comment.getId();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(mongoTemplate.findOne(any(), eq(Profile.class))).thenReturn(profile);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment deletedComment = commentService.deleteComment(commentId, user);

        assertNotNull(deletedComment);
        assertTrue(deletedComment.getIsDeleted());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void getUserCommentListTest() {
        List<Comment> comments = Arrays.asList(comment);
        when(mongoTemplate.find(any(Query.class), eq(Comment.class))).thenReturn(comments);

        List<Comment> userComments = commentService.getUserCommentList(user);

        assertNotNull(userComments);
        assertFalse(userComments.isEmpty());
        assertEquals(1, userComments.size());
        assertEquals(comment, userComments.get(0));
    }
}
