package com.app.gamereview.controller;

import com.app.gamereview.dto.request.review.CreateReviewRequestDto;
import com.app.gamereview.dto.request.review.GetAllReviewsFilterRequestDto;
import com.app.gamereview.dto.request.review.UpdateReviewRequestDto;
import com.app.gamereview.dto.response.review.GetAllReviewsResponseDto;
import com.app.gamereview.model.Review;
import com.app.gamereview.model.User;
import com.app.gamereview.service.ReviewService;
import com.app.gamereview.util.JwtUtil;
import com.app.gamereview.util.validation.annotation.AuthorizationRequired;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@Validated
public class ReviewController {

	private final ReviewService reviewService;

	@Autowired
	public ReviewController(
			ReviewService reviewService
	) {
		this.reviewService = reviewService;
	}

	@GetMapping("/get-all")
	public ResponseEntity<List<GetAllReviewsResponseDto>> getReviews(
			@ParameterObject GetAllReviewsFilterRequestDto filter,
			@RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String Authorization) {

		String email;
		if (JwtUtil.validateToken(Authorization)) email = JwtUtil.extractSubject(Authorization);
		else email = "";

		List<GetAllReviewsResponseDto> reviews = reviewService.getAllReviews(filter, email);
		return ResponseEntity.ok(reviews);
	}

	@GetMapping("/get")
	public ResponseEntity<GetAllReviewsResponseDto> getReview(@RequestParam String id) {
		GetAllReviewsResponseDto review = reviewService.getReview(id);

		return ResponseEntity.ok(review);
	}

	@AuthorizationRequired
	@PostMapping("/create")
	public ResponseEntity<Review> createReview(@Valid @RequestBody CreateReviewRequestDto createReviewRequestDto,
											@RequestHeader String Authorization, HttpServletRequest request) {
		User user = (User) request.getAttribute("authenticatedUser");
		Review reviewToCreate = reviewService.addReview(createReviewRequestDto, user);
		return ResponseEntity.ok(reviewToCreate);
	}

	@AuthorizationRequired
	@PutMapping("/update")
	public ResponseEntity<Boolean> updateReview(
			@RequestParam String id,
			@Valid @RequestBody UpdateReviewRequestDto updateReviewRequestDto,
			@RequestHeader String Authorization, HttpServletRequest request) {

		User user = (User) request.getAttribute("authenticatedUser");

		Boolean isAck = reviewService.updateReview(id, updateReviewRequestDto, user);
		return ResponseEntity.ok(isAck);
	}

	@AuthorizationRequired
	@DeleteMapping("/delete")
	public ResponseEntity<Boolean> deleteReview(@RequestParam String id,
			@RequestHeader String Authorization, HttpServletRequest request) {
		User user = (User) request.getAttribute("authenticatedUser");
		Boolean isAck = reviewService.deleteReview(id,user);
		return ResponseEntity.ok(isAck);
	}

}
