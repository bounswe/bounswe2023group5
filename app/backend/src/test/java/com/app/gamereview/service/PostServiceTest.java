package com.app.gamereview.service;

import com.app.gamereview.dto.request.home.HomePagePostsFilterRequestDto;
import com.app.gamereview.dto.request.notification.CreateNotificationRequestDto;
import com.app.gamereview.dto.request.post.CreatePostRequestDto;
import com.app.gamereview.dto.request.post.EditPostRequestDto;
import com.app.gamereview.dto.request.post.GetPostListFilterRequestDto;
import com.app.gamereview.dto.response.comment.GetPostCommentsResponseDto;
import com.app.gamereview.dto.response.home.HomePagePostResponseDto;
import com.app.gamereview.dto.response.post.GetPostDetailResponseDto;
import com.app.gamereview.dto.response.post.GetPostListResponseDto;
import com.app.gamereview.enums.ForumType;
import com.app.gamereview.enums.SortDirection;
import com.app.gamereview.enums.SortType;
import com.app.gamereview.enums.VoteChoice;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.*;
import com.app.gamereview.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.bson.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private GameRepository gameRepository;

    @Mock
    private GroupRepository groupRepository;

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
        assertThrows(ResourceNotFoundException.class,
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

    @Test
    void testGetCommentList_Success() {
        // Arrange
        String postId = "postId";
        String userId = "userId";
        User user = new User();
        user.setId(userId);

        // Mock data
        Post post = new Post();
        post.setId(postId);
        post.setForum("forumId");

        Forum forum = new Forum();
        forum.setId("forumId");
        forum.setBannedUsers(List.of(""));

        Comment comment1 = new Comment();
        comment1.setId("commentId1");
        comment1.setCreatedAt(LocalDateTime.now());
        comment1.setCommenter("userId");
        comment1.setParentComment(null);
        comment1.setCommentContent("");
        comment1.setPost(postId);
        comment1.setLastEditedAt(LocalDateTime.now());
        comment1.setIsDeleted(false);
        comment1.setOverallVote(5);
        comment1.setVoteCount(10);


        Comment comment2 = new Comment();
        comment2.setId("commentId2");
        comment2.setCreatedAt(LocalDateTime.now());
        comment2.setCommenter("userId");
        comment2.setCommentContent("");
        comment2.setPost(postId);
        comment2.setLastEditedAt(LocalDateTime.now());
        comment2.setIsDeleted(false);
        comment2.setOverallVote(5);
        comment2.setVoteCount(10);
        comment2.setParentComment("commentId1");

        Vote vote = new Vote();
        vote.setId("voteId");
        vote.setChoice(VoteChoice.UPVOTE);

        List<Comment> comments = List.of(comment1, comment2);

        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));
        when(forumRepository.findById(anyString())).thenReturn(Optional.of(forum));
        when(commentRepository.findByPost(anyString())).thenReturn(comments);
        when(userRepository.findByIdAndIsDeletedFalse(any(String.class))).thenReturn(Optional.of(user));
        when(voteRepository.findByTypeIdAndVotedBy(any(String.class), any(String.class))).thenReturn(Optional.of(vote));

        List<GetPostCommentsResponseDto> result = postService.getCommentList(postId, userId);

        assertNotNull(result);
        assertEquals(1, result.size()); // Assuming only one top-level comment in the test data
        assertEquals(comment1.getId(), result.get(0).getId());
        assertEquals(1, result.get(0).getReplies().size()); // Assuming one reply in the test data
        assertEquals(comment2.getId(), result.get(0).getReplies().get(0).getId());

        verify(postRepository, times(1)).findById(anyString());
        verify(forumRepository, times(1)).findById(anyString());
        verify(commentRepository, times(1)).findByPost(anyString());
    }

    @Test
    void testGetHomePagePostsOfUser_Success() {
        HomePagePostsFilterRequestDto filter = new HomePagePostsFilterRequestDto();
        User user = new User();
        user.setId("userId");

        Profile profile = new Profile();
        profile.setUserId(user.getId());
        profile.setGames(Collections.singletonList("gameId"));

        Game game = new Game();
        game.setId("gameId");
        game.setForum("gameForumId");

        Group group = new Group();
        group.setId("groupId");
        group.setForumId("groupForumId");

        Forum forum = new Forum();
        forum.setId("forumId");
        forum.setType(ForumType.GAME);
        forum.setParent("gameId");

        Post post = new Post();
        post.setId("postId");
        post.setForum("gameForumId");
        post.setTags(List.of("tagId"));

        Post extraPost = new Post();
        extraPost.setId("extraPostId");
        extraPost.setForum("extraGameForumId");
        extraPost.setTags(List.of("tagId"));

        Tag randomTag = new Tag();
        randomTag.setId("tagId");

        HomePagePostResponseDto responseDto = new HomePagePostResponseDto();
        responseDto.setTags(List.of(randomTag));

        when(profileRepository.findByUserIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(profile));
        when(gameRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(game));
        when(groupRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(group));
        when(postRepository.findByForumAndIsDeletedFalse(anyString())).thenReturn(Collections.singletonList(post));
        when(forumRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(forum));
        when(mongoTemplate.find(any(Query.class), eq(Group.class))).thenReturn(Collections.singletonList(group));
        when(mongoTemplate.find(any(Query.class), eq(Post.class))).thenReturn(Collections.singletonList(extraPost));
        when(modelMapper.map(any(Post.class), eq(HomePagePostResponseDto.class))).thenReturn(responseDto);

        List<HomePagePostResponseDto> result = postService.getHomePagePostsOfUser(filter, user);

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void testGetHomePagePostsOfUser_Success_Group_Forum() {
        HomePagePostsFilterRequestDto filter = new HomePagePostsFilterRequestDto();
        filter.setSortBy(SortType.OVERALL_VOTE.name());
        filter.setSortDirection(SortDirection.ASCENDING.name());

        User user = new User();
        user.setId("userId");

        Profile profile = new Profile();
        profile.setUserId(user.getId());
        profile.setGames(Collections.singletonList("gameId"));

        Game game = new Game();
        game.setId("gameId");
        game.setForum("gameForumId");

        Group group = new Group();
        group.setId("groupId");
        group.setForumId("groupForumId");

        Forum forum = new Forum();
        forum.setId("forumId");
        forum.setType(ForumType.GAME);
        forum.setParent("gameId");

        Post post = new Post();
        post.setId("postId");
        post.setForum("gameForumId");
        post.setTags(List.of("tagId"));

        Post extraPost = new Post();
        extraPost.setId("extraPostId");
        extraPost.setForum("extraGameForumId");
        extraPost.setTags(List.of("tagId"));

        Tag randomTag = new Tag();
        randomTag.setId("tagId");

        HomePagePostResponseDto responseDto = new HomePagePostResponseDto();
        responseDto.setTags(List.of(randomTag));

        when(profileRepository.findByUserIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(profile));
        when(gameRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(game));
        when(groupRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(group));
        when(postRepository.findByForumAndIsDeletedFalse(anyString())).thenReturn(Collections.singletonList(post));
        when(forumRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(forum));
        when(mongoTemplate.find(any(Query.class), eq(Group.class))).thenReturn(Collections.singletonList(group));
        when(mongoTemplate.find(any(Query.class), eq(Post.class))).thenReturn(Collections.singletonList(extraPost));
        when(modelMapper.map(any(Post.class), eq(HomePagePostResponseDto.class))).thenReturn(responseDto);

        List<HomePagePostResponseDto> result = postService.getHomePagePostsOfUser(filter, user);

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void testGetHomePagePostsOfGuest_Success() {
        HomePagePostsFilterRequestDto filter = new HomePagePostsFilterRequestDto();
        User user = new User();
        user.setId("userId");

        Profile profile = new Profile();
        profile.setUserId(user.getId());
        profile.setGames(Collections.singletonList("gameId"));

        Game game = new Game();
        game.setId("gameId");
        game.setForum("gameForumId");

        Group group = new Group();
        group.setId("groupId");
        group.setForumId("groupForumId");

        Forum forum = new Forum();
        forum.setId("forumId");
        forum.setType(ForumType.GAME);
        forum.setParent("gameId");

        Post post = new Post();
        post.setId("postId");
        post.setForum("gameForumId");
        post.setTags(List.of("tagId"));

        Post extraPost = new Post();
        extraPost.setId("extraPostId");
        extraPost.setForum("extraGameForumId");
        extraPost.setTags(List.of("tagId"));

        Tag randomTag = new Tag();
        randomTag.setId("tagId");

        HomePagePostResponseDto responseDto = new HomePagePostResponseDto();
        responseDto.setTags(List.of(randomTag));

        when(profileRepository.findByUserIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(profile));
        when(gameRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(game));
        when(groupRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(group));
        when(postRepository.findByForumAndIsDeletedFalse(anyString())).thenReturn(Collections.singletonList(post));
        when(forumRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(forum));
        when(mongoTemplate.find(any(Query.class), eq(Group.class))).thenReturn(Collections.singletonList(group));
        when(mongoTemplate.find(any(Query.class), eq(Post.class))).thenReturn(Collections.singletonList(extraPost));
        when(mongoTemplate.find(any(Query.class), eq(Forum.class))).thenReturn(Collections.singletonList(forum));
        when(modelMapper.map(any(Post.class), eq(HomePagePostResponseDto.class))).thenReturn(responseDto);

        List<HomePagePostResponseDto> result = postService.getHomePagePostsOfGuest(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetHomePagePostsOfGuest_Success_Group_Forum() {
        HomePagePostsFilterRequestDto filter = new HomePagePostsFilterRequestDto();
        filter.setSortBy(SortType.OVERALL_VOTE.name());
        filter.setSortDirection(SortDirection.ASCENDING.name());

        User user = new User();
        user.setId("userId");

        Profile profile = new Profile();
        profile.setUserId(user.getId());
        profile.setGames(Collections.singletonList("gameId"));

        Game game = new Game();
        game.setId("gameId");
        game.setForum("gameForumId");

        Group group = new Group();
        group.setId("groupId");
        group.setForumId("groupForumId");

        Forum forum = new Forum();
        forum.setId("forumId");
        forum.setType(ForumType.GROUP);
        forum.setParent("gameId");

        Post post = new Post();
        post.setId("postId");
        post.setForum("gameForumId");
        post.setTags(List.of("tagId"));

        Post extraPost = new Post();
        extraPost.setId("extraPostId");
        extraPost.setForum("extraGameForumId");
        extraPost.setTags(List.of("tagId"));

        Tag randomTag = new Tag();
        randomTag.setId("tagId");

        HomePagePostResponseDto responseDto = new HomePagePostResponseDto();
        responseDto.setTags(List.of(randomTag));

        when(profileRepository.findByUserIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(profile));
        when(gameRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(game));
        when(groupRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(group));
        when(postRepository.findByForumAndIsDeletedFalse(anyString())).thenReturn(Collections.singletonList(post));
        when(forumRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(forum));
        when(mongoTemplate.find(any(Query.class), eq(Group.class))).thenReturn(Collections.singletonList(group));
        when(mongoTemplate.find(any(Query.class), eq(Post.class))).thenReturn(Collections.singletonList(extraPost));
        when(mongoTemplate.find(any(Query.class), eq(Forum.class))).thenReturn(Collections.singletonList(forum));
        when(modelMapper.map(any(Post.class), eq(HomePagePostResponseDto.class))).thenReturn(responseDto);

        List<HomePagePostResponseDto> result = postService.getHomePagePostsOfGuest(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetHomepagePosts_UserNotFound() {
        HomePagePostsFilterRequestDto filter = new HomePagePostsFilterRequestDto();
        String email = "nonexistentuser@example.com";

        when(userRepository.findByEmailAndIsDeletedFalse(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> postService.getHomepagePosts(filter, email),
                "Should throw ResourceNotFoundException when user is not found");

        verify(userRepository, times(1)).findByEmailAndIsDeletedFalse(anyString());
    }

    @Test
    void testGetHomepagePosts_Guest() {
        HomePagePostsFilterRequestDto filter = new HomePagePostsFilterRequestDto();

        List<HomePagePostResponseDto> result = postService.getHomepagePosts(filter, null);

        verifyNoInteractions(userRepository);
    }
}
