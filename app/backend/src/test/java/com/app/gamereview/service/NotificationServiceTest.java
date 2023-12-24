package com.app.gamereview.service;

import com.app.gamereview.dto.request.notification.CreateNotificationRequestDto;
import com.app.gamereview.dto.request.notification.GetNotificationsRequestDto;
import com.app.gamereview.model.Notification;
import com.app.gamereview.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;

    @BeforeEach
    public void setup() {
        notification = new Notification();
        notification.setId("123");
        notification.setUser("user1");
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
    }

    @Test
    public void testGetNotificationList() {
        when(mongoTemplate.find(any(Query.class), eq(Notification.class))).thenReturn(Arrays.asList(notification));

        GetNotificationsRequestDto requestDto = new GetNotificationsRequestDto();
        requestDto.setIsRead(false);

        List<Notification> result = notificationService.getNotificationList("user1", requestDto);

        assertFalse(result.isEmpty());
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Notification.class));
    }

    @Test
    public void testCreateNotification() {
        CreateNotificationRequestDto requestDto = new CreateNotificationRequestDto();
        requestDto.setUser("user1");

        when(modelMapper.map(requestDto, Notification.class)).thenReturn(notification);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.createNotification(requestDto);

        assertNotNull(result);
        assertEquals("user1", result.getUser());
        assertFalse(result.getIsRead());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    public void testGetNotificationListWithNullFilter() {
        // Setup notifications
        Notification unreadNotification = new Notification();
        unreadNotification.setId("notif1");
        unreadNotification.setUser("user1");
        unreadNotification.setIsRead(false);

        Notification readNotification = new Notification();
        readNotification.setId("notif2");
        readNotification.setUser("user1");
        readNotification.setIsRead(true);

        // Setup the returned list from mongoTemplate
        when(mongoTemplate.find(any(Query.class), eq(Notification.class)))
                .thenReturn(Arrays.asList(unreadNotification, readNotification));

        // Call the method with null filter
        List<Notification> result = notificationService.getNotificationList("user1", null);

        // Assertions and Verifications
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        verify(notificationRepository, times(1)).save(unreadNotification); // Only the unread notification should be saved
        verify(notificationRepository, never()).save(readNotification); // Read notification should not be saved
    }
}