package com.app.gamereview.controller;

import com.app.gamereview.dto.request.review.CreateReviewRequestDto;
import com.app.gamereview.dto.request.review.GetAllReviewsFilterRequestDto;
import com.app.gamereview.dto.request.review.UpdateReviewRequestDto;
import com.app.gamereview.model.Review;
import com.app.gamereview.model.User;
import com.app.gamereview.service.ReviewService;
import com.app.gamereview.util.validation.annotation.AuthorizationRequired;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
	public ResponseEntity<List<Review>> getReviews(GetAllReviewsFilterRequestDto filter) {
		List<Review> reviews = reviewService.getAllReviews(filter);
		return ResponseEntity.ok(reviews);
	}

	@GetMapping("/get")
	public ResponseEntity<Review> getReview(@RequestParam String id) {
		Review review = reviewService.getReview(id);

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

	@PutMapping("/update")
	public ResponseEntity<Boolean> updateReview(
			@RequestParam String id,
			@Valid @RequestBody UpdateReviewRequestDto updateReviewRequestDto) {

		Boolean isAck = reviewService.updateReview(id, updateReviewRequestDto);
		return ResponseEntity.ok(isAck);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Boolean> deleteReview(@RequestParam String id) {
		Boolean isAck = reviewService.deleteReview(id);
		return ResponseEntity.ok(isAck);
	}

}
