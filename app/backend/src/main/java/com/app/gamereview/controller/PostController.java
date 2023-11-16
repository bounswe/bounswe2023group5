package com.app.gamereview.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.app.gamereview.dto.request.post.EditPostRequestDto;
import com.app.gamereview.dto.response.comment.GetPostCommentsResponseDto;
import com.app.gamereview.dto.response.post.GetPostDetailResponseDto;
import com.app.gamereview.model.User;
import com.app.gamereview.service.FileStorageService;
import com.app.gamereview.util.validation.annotation.AuthorizationRequired;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/post")
@Validated
public class PostController {

    @Value("${SECRET_KEY}")
    private String secret_key = "${SECRET_KEY}";

    private final PostService postService;

    private final FileStorageService fileService;

    @Autowired
    public PostController(PostService postService, FileStorageService fileService) {
        this.postService = postService;
        this.fileService = fileService;
    }

    @GetMapping("/get-post-list")
    public ResponseEntity<List<GetPostListResponseDto>> getPostList(@Valid @ParameterObject GetPostListFilterRequestDto filter, @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String Authorization) {

        String email = "";
        try {
            Claims claims = Jwts.parser().setSigningKey(secret_key).parseClaimsJws(String.valueOf(Authorization)).getBody();

            Date expirationDate = claims.getExpiration();
            Date now = new Date();
            if (expirationDate.after(now)) {
                email = claims.getSubject();
            }
        }
        catch (SignatureException | ExpiredJwtException | IllegalArgumentException | MalformedJwtException e) {
            // SignatureException: Token signature is invalid
            // ExpiredJwtException: Token has expired
            // IllegalArgumentException: Token is not correctly formatted
            // MalformedJwtException: Token is not correctly formatted (eg. empty string)
            // In any of these cases, the token is considered invalid
        }

        List<GetPostListResponseDto> posts = postService.getPostList(filter, email);
        return ResponseEntity.ok(posts);
    }

    @AuthorizationRequired
    @GetMapping("/get-post-detail")
    public ResponseEntity<GetPostDetailResponseDto> getPostDetail(@RequestParam String id, @RequestHeader String Authorization, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        GetPostDetailResponseDto post = postService.getPostById(id, user);
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
