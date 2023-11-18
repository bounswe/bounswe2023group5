package com.app.gamereview.controller;

import com.app.gamereview.dto.request.group.CreateGroupRequestDto;
import com.app.gamereview.dto.request.group.GetAllGroupsFilterRequestDto;
import com.app.gamereview.dto.request.review.CreateReviewRequestDto;
import com.app.gamereview.dto.request.review.GetAllReviewsFilterRequestDto;
import com.app.gamereview.dto.request.review.UpdateReviewRequestDto;
import com.app.gamereview.dto.response.review.GetAllReviewsResponseDto;
import com.app.gamereview.model.Group;
import com.app.gamereview.model.Review;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.GroupRepository;
import com.app.gamereview.service.GroupService;
import com.app.gamereview.service.ReviewService;
import com.app.gamereview.util.validation.annotation.AuthorizationRequired;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/group")
@Validated
public class GroupController {

	@Value("${SECRET_KEY}")
	private String secret_key = "${SECRET_KEY}";

	private final GroupService groupService;

	@Autowired
	public GroupController(
			GroupService groupService
	) {
		this.groupService = groupService;
	}

	@GetMapping("/get-all")
	public ResponseEntity<List<Group>> getReviews(
			@ParameterObject GetAllGroupsFilterRequestDto filter) {
		List<Group> groups = groupService.getAllGroups(filter);
		return ResponseEntity.ok(groups);
	}

	@GetMapping("/get")
	public ResponseEntity<Group> getGroup(@RequestParam String id) {
		Group group = groupService.getGroupById(id);

		return ResponseEntity.ok(group);
	}

	@AuthorizationRequired
	@PostMapping("/create")
	public ResponseEntity<Group> createGroup(@Valid @RequestBody CreateGroupRequestDto createGroupRequestDto,
											@RequestHeader String Authorization, HttpServletRequest request) {
		User user = (User) request.getAttribute("authenticatedUser");
		Group groupToCreate = groupService.createGroup(createGroupRequestDto, user);
		return ResponseEntity.ok(groupToCreate);
	}

}
