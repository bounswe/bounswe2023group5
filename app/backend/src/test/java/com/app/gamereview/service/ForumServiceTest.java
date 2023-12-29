package com.app.gamereview.service;

import com.app.gamereview.enums.ForumType;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.Forum;
import com.app.gamereview.repository.ForumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ForumServiceTest {

    @Mock
    private ForumRepository forumRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ForumService forumService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void banUser_SuccessfullyBanned() {
        // Arrange
        String forumId = "forum123";
        String userId = "user456";
        Forum forum = new Forum();
        forum.setId(forumId);
        forum.setName("ForumName");
        forum.setType(ForumType.GROUP);
        forum.setParent("ParentGroup");
        forum.setSubscribers(new ArrayList<>());
        forum.setBannedUsers(new ArrayList<>());
        when(forumRepository.findById(forumId)).thenReturn(Optional.of(forum));

        // Act
        boolean result = forumService.banUser(forumId, userId);

        // Assert
        assertTrue(result);
        assertTrue(forum.getBannedUsers().contains(userId));
        verify(forumRepository, times(1)).save(forum);
    }

    @Test
    void banUser_ForumNotFound() {
        // Arrange
        String forumId = "nonExistentForum";
        String userId = "user123";
        when(forumRepository.findById(forumId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> forumService.banUser(forumId, userId));
        verify(forumRepository, never()).save(any());
    }

    @Test
    void unbanUser_SuccessfullyUnbanned() {
        // Arrange
        String forumId = "forum789";
        String userId = "user987";
        Forum forum = new Forum();
        forum.setId(forumId);
        forum.setName("AnotherForum");
        forum.setType(ForumType.GAME);
        forum.setParent("ParentGame");
        forum.setSubscribers(new ArrayList<>());
        forum.setBannedUsers(new ArrayList<>(List.of(userId)));
        when(forumRepository.findById(forumId)).thenReturn(Optional.of(forum));

        // Act
        boolean result = forumService.unbanUser(forumId, userId);

        // Assert
        assertTrue(result);
        assertFalse(forum.getBannedUsers().contains(userId));
        verify(forumRepository, times(1)).save(forum);
    }

    @Test
    void unbanUser_ForumNotFound() {
        // Arrange
        String forumId = "nonExistentForum";
        String userId = "user789";
        when(forumRepository.findById(forumId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> forumService.unbanUser(forumId, userId));
        verify(forumRepository, never()).save(any());
    }
}
