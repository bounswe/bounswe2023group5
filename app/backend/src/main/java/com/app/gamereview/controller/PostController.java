package com.app.gamereview.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<List<GetPostListResponseDto>> getPostList(GetPostListFilterRequestDto filter) {
    List<GetPostListResponseDto> posts = postService.getPostList(filter);
    return ResponseEntity.ok(posts);
  }

  @GetMapping("/get-post-detail")
  public ResponseEntity<Post> getPostDetail(@RequestParam String id) {
    Post post = postService.getPostById(id);
    return ResponseEntity.ok(post);
  }

  @PostMapping("/create")
  public ResponseEntity<Post> createPost(@Valid @RequestBody CreatePostRequestDto postDto) {
    Post postToCreate = postService.createPost(postDto);
    return ResponseEntity.ok(postToCreate);
  }
}
