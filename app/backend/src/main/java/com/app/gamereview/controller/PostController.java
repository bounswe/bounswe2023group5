package com.app.gamereview.controller;

import java.util.List;

import com.app.gamereview.dto.request.post.EditPostRequestDto;
import com.app.gamereview.dto.response.comment.GetPostCommentsResponseDto;
import com.app.gamereview.dto.response.post.GetPostDetailResponseDto;
import com.app.gamereview.model.User;
import com.app.gamereview.util.JwtUtil;
import com.app.gamereview.util.validation.annotation.AuthorizationRequired;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.app.gamereview.dto.request.post.CreatePostRequestDto;
import com.app.gamereview.dto.request.post.GetPostListFilterRequestDto;
import com.app.gamereview.dto.response.post.GetPostListResponseDto;
import com.app.gamereview.model.Post;
import com.app.gamereview.service.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/post")
@Validated
public class PostController {


    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/get-post-list")
    public ResponseEntity<List<GetPostListResponseDto>> getPostList(@Valid @ParameterObject GetPostListFilterRequestDto filter, @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String Authorization) {

        String email;
        if (JwtUtil.validateToken(Authorization)) email = JwtUtil.extractSubject(Authorization);
        else email = "";

        List<GetPostListResponseDto> posts = postService.getPostList(filter, email);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/get-post-detail")
    public ResponseEntity<GetPostDetailResponseDto> getPostDetail(@RequestParam String id, @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String Authorization) {
        String email;
        if (JwtUtil.validateToken(Authorization)) email = JwtUtil.extractSubject(Authorization);
        else email = "";
        GetPostDetailResponseDto post = postService.getPostById(id, email);
        return ResponseEntity.ok(post);
    }

    @AuthorizationRequired
    @GetMapping("/get-post-comments")
    public ResponseEntity<List<GetPostCommentsResponseDto>> getPostComments(@RequestParam String id, @RequestHeader String Authorization, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        List<GetPostCommentsResponseDto> comments = postService.getCommentList(id, user);
        return ResponseEntity.ok(comments);
    }

    @AuthorizationRequired
    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@Valid @RequestBody CreatePostRequestDto post,
                                           @RequestHeader String Authorization, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        Post postToCreate = postService.createPost(post, user);
        return ResponseEntity.ok(postToCreate);
    }

    @AuthorizationRequired
    @PostMapping("/edit")
    public ResponseEntity<Post> editPost(@RequestParam String id, @Valid @RequestBody EditPostRequestDto post, @RequestHeader String Authorization, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        Post editedPost = postService.editPost(id, post, user);
        return ResponseEntity.ok(editedPost);
    }

    @AuthorizationRequired
    @DeleteMapping("/delete")
    public ResponseEntity<Post> deletePost(@RequestParam String id, @RequestHeader String Authorization, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        Post deletedPost = postService.deletePost(id, user);
        return ResponseEntity.ok(deletedPost);
    }
}
