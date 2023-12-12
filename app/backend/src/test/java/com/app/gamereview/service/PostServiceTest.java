package com.app.gamereview.service;

import com.app.gamereview.dto.request.notification.CreateNotificationRequestDto;
import com.app.gamereview.dto.request.post.CreatePostRequestDto;
import com.app.gamereview.dto.request.post.EditPostRequestDto;
import com.app.gamereview.dto.request.post.GetPostListFilterRequestDto;
import com.app.gamereview.dto.response.post.GetPostDetailResponseDto;
import com.app.gamereview.dto.response.post.GetPostListResponseDto;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.*;
import com.app.gamereview.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.mockito.*;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ForumRepository forumRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PostService postService;
    @Mock
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPostList_Success() {
        // Arrange
        GetPostListFilterRequestDto filter = new GetPostListFilterRequestDto();
        String email = "user@example.com";
        User loggedInUser = new User();
        loggedInUser.setId("userId");
        loggedInUser.setEmail(email);

        when(userRepository.findByEmailAndIsDeletedFalse(anyString())).thenReturn(Optional.of(loggedInUser));
        when(mongoTemplate.find(any(Query.class), eq(Post.class))).thenReturn(Collections.emptyList());

        // Act
        List<GetPostListResponseDto> result = postService.getPostList(filter, email);

        // Assert
        Assertions.assertNotNull(result);
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Post.class));
    }

    @Test
    void testGetPostById_Success() {
        // Arrange
        String postId = "postId";
        String email = "user@example.com";
        User loggedInUser = new User();
        loggedInUser.setId("userId");
        loggedInUser.setEmail(email);
        Post post = new Post();
        ArrayList<String> tagList = new ArrayList<>();
        tagList.add("tag");
        post.setTags(tagList);
        post.setAchievement("");
        post.setCharacter("");

        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));
        when(userRepository.findByEmailAndIsDeletedFalse(anyString())).thenReturn(Optional.of(loggedInUser));
        when(forumRepository.findById(anyString())).thenReturn(Optional.of(new Forum()));
        when(voteRepository.findByTypeIdAndVotedBy(anyString(), anyString())).thenReturn(Optional.empty());
        when(achievementRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.empty());
        when(characterRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.empty());
        when(tagRepository.findById(anyString())).thenReturn(Optional.empty());
        when(modelMapper.map(any(Optional.class), eq(GetPostDetailResponseDto.class))).thenReturn(new GetPostDetailResponseDto());

        // Act
        GetPostDetailResponseDto result = postService.getPostById(postId, email);

        // Assert
        Assertions.assertNotNull(result);
        verify(voteRepository, times(1)).findByTypeIdAndVotedBy(anyString(), anyString());
        verify(achievementRepository, times(1)).findByIdAndIsDeletedFalse(anyString());
        verify(characterRepository, times(1)).findByIdAndIsDeletedFalse(anyString());
        verify(tagRepository, times(1)).findById(anyString());
    }

    @Test
    void testGetPostById_NotFound() {
        // Arrange
        String postId = "nonExistentPostId";
        String email = "user@example.com";
        User loggedInUser = new User();
        loggedInUser.setId("userId");
        loggedInUser.setEmail(email);

        when(postRepository.findById(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmailAndIsDeletedFalse(anyString())).thenReturn(Optional.of(loggedInUser));

        // Act and Assert
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> postService.getPostById(postId, email),
                "Should throw ResourceNotFoundException when post is not found");

        // Verify
        verify(postRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).findByEmailAndIsDeletedFalse(anyString());
    }

    @Test
    void testCreatePost_Success() {
        // Arrange
        CreatePostRequestDto request = new CreatePostRequestDto();
        Forum forum = new Forum();
        forum.setId("forumId");
        forum.setName("forumName");
        request.setForum("forumId");
        request.setTags(Collections.singletonList("tagId"));
        User user = new User();
        user.setId("userId");
        user.setUsername("username");
        Profile profile = new Profile();
        profile.setUserId(user.getId());

        Post post = new Post();

        Notification notification = new Notification();

        when(forumRepository.findById(anyString())).thenReturn(Optional.of(forum));
        when(achievementRepository.findById(anyString())).thenReturn(Optional.of(new Achievement()));
        when(tagRepository.findById(anyString())).thenReturn(Optional.of(new Tag()));
        when(userRepository.findByEmailAndIsDeletedFalse(anyString())).thenReturn(Optional.of(user));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(notificationService.createNotification(any(CreateNotificationRequestDto.class))).thenReturn(notification);
        when(mongoTemplate.findOne(any(Query.class), eq(Profile.class))).thenReturn(profile);
        when(modelMapper.map(any(CreatePostRequestDto.class), eq(Post.class))).thenReturn(post);
        // Act
        Post result = postService.createPost(request, user);

        // Assert
        Assertions.assertNotNull(result);
        verify(profileRepository, times(1)).save(any(Profile.class));
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testEditPost_Success() {
        // Arrange
        String postId = "postId";
        EditPostRequestDto request = new EditPostRequestDto();
        request.setTitle("New Title");
        User user = new User();
        user.setId("userId");
        Post post = new Post();
        post.setId(postId);
        post.setPoster(user.getId());
        post.setTitle("old title");

        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));
        when(userRepository.findByEmailAndIsDeletedFalse(anyString())).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // Act
        Post result = postService.editPost(postId, request, user);

        // Assert
        Assertions.assertNotNull(result);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testDeletePost_Success() {
        // Arrange
        String postId = "postId";
        User user = new User();
        user.setId("userId");
        Post existingPost = new Post();
        existingPost.setId(postId);
        existingPost.setPoster(user.getId());

        when(postRepository.findById(anyString())).thenReturn(Optional.of(existingPost));
        when(userRepository.findByEmailAndIsDeletedFalse(anyString())).thenReturn(Optional.of(user));
        when(mongoTemplate.findOne(any(Query.class), eq(Profile.class))).thenReturn(new Profile());
        when(profileRepository.save(any(Profile.class))).thenReturn(new Profile());
        when(postRepository.save(any(Post.class))).thenReturn(existingPost);

        // Act
        Post result = postService.deletePost(postId, user);

        // Assert
        Assertions.assertNotNull(result);
        verify(profileRepository, times(1)).save(any(Profile.class));
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testGetUserPostList_Success() {
        // Arrange
        User user = new User();
        user.setId("userId");

        when(mongoTemplate.find(any(Query.class), eq(Post.class))).thenReturn(Collections.emptyList());

        // Act
        List<Post> result = postService.getUserPostList(user);

        // Assert
        Assertions.assertNotNull(result);
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Post.class));
    }
}
