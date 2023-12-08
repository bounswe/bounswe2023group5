package com.app.gamereview.controller;

import com.app.gamereview.dto.request.post.GetPostListFilterRequestDto;
import com.app.gamereview.dto.response.comment.GetPostCommentsResponseDto;
import com.app.gamereview.dto.response.post.GetPostListResponseDto;
import com.app.gamereview.model.Notification;
import com.app.gamereview.model.User;
import com.app.gamereview.service.NotificationService;
import com.app.gamereview.service.PostService;
import com.app.gamereview.util.JwtUtil;
import com.app.gamereview.util.validation.annotation.AuthorizationRequired;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<Notification>> getNotificationList( @RequestHeader String Authorization, HttpServletRequest request) {

        User user = (User) request.getAttribute("authenticatedUser");
        List<Notification> notifications = notificationService.getNotificationList(user.getId());
        return ResponseEntity.ok(notifications);
    }
}
