package com.app.gamereview.controller;

import com.app.gamereview.dto.request.home.HomePagePostsFilterRequestDto;
import com.app.gamereview.dto.response.home.HomePagePostResponseDto;
import com.app.gamereview.model.Post;
import com.app.gamereview.model.User;
import com.app.gamereview.service.PostService;
import com.app.gamereview.util.JwtUtil;
import com.app.gamereview.util.validation.annotation.AuthorizationRequired;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

	@GetMapping
	public ResponseEntity<List<HomePagePostResponseDto>> getHomePagePosts(@ParameterObject HomePagePostsFilterRequestDto filter,
													   @RequestHeader(name = HttpHeaders.AUTHORIZATION,
															   required = false) String Authorization){
		String email = null;
		if (JwtUtil.validateToken(Authorization))
			email = JwtUtil.extractSubject(Authorization);
		List<HomePagePostResponseDto> postsToShow = postService.getHomepagePosts(filter, email);
		return ResponseEntity.ok(postsToShow);
	}

}
