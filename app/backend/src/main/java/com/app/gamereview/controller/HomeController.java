package com.app.gamereview.controller;

import com.app.gamereview.dto.request.home.HomePagePostsFilterRequestDto;
import com.app.gamereview.model.Post;
import com.app.gamereview.model.User;
import com.app.gamereview.service.PostService;
import com.app.gamereview.util.validation.annotation.AuthorizationRequired;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/home")
@Validated
public class HomeController {

	private final PostService postService;

	@Autowired
	public HomeController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping("/unsigned")
	public ResponseEntity<List<Post>> getHomePagePostsForGuest(@ParameterObject HomePagePostsFilterRequestDto filter){
		List<Post> postsToShow = postService.getHomePagePosts(filter);
		return ResponseEntity.ok(postsToShow);
	}

	@AuthorizationRequired
	@GetMapping()
	public ResponseEntity<List<Post>> getHomePagePosts(@ParameterObject HomePagePostsFilterRequestDto filter,
													   @RequestHeader String Authorization, HttpServletRequest request){
		User user = (User) request.getAttribute("authenticatedUser");
		List<Post> postsToShow = postService.getHomePagePostsOfUser(filter, user);
		return ResponseEntity.ok(postsToShow);
	}

}
