package com.app.gamereview.controller;

import com.app.gamereview.dto.request.notification.GetNotificationsRequestDto;
import com.app.gamereview.model.Notification;
import com.app.gamereview.model.User;
import com.app.gamereview.service.NotificationService;
import com.app.gamereview.util.validation.annotation.AuthorizationRequired;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @AuthorizationRequired
    @GetMapping("/get-notifications")
    public ResponseEntity<List<Notification>> getNotificationList(@RequestHeader String Authorization, @RequestBody(required = false) GetNotificationsRequestDto filter, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        List<Notification> notifications = notificationService.getNotificationList(user.getId(), filter);
        return ResponseEntity.ok(notifications);
    }
}
