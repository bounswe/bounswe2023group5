package com.app.gamereview.controller;

import com.app.gamereview.dto.request.post.GetPostListFilterRequestDto;
import com.app.gamereview.dto.response.post.GetPostListResponseDto;
import com.app.gamereview.model.Post;
import com.app.gamereview.model.User;
import com.app.gamereview.service.ForumService;
import com.app.gamereview.service.PostService;
import com.app.gamereview.util.validation.annotation.AdminRequired;
import com.app.gamereview.util.validation.annotation.AuthorizationRequired;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/forum")
@Validated
public class ForumController {

    private final ForumService forumService;

    @Autowired
    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @AuthorizationRequired
    @AdminRequired
    @PutMapping("/ban-user")
    public ResponseEntity<Boolean> banUser(@RequestParam String forumId, @RequestParam String userId, @RequestHeader String Authorization, HttpServletRequest request) {
        Boolean result = forumService.banUser(forumId, userId);
        return ResponseEntity.ok(result);
    }
}
