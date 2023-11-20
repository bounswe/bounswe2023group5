package com.app.gamereview.controller;

import com.app.gamereview.dto.request.group.AddGroupTagRequestDto;
import com.app.gamereview.dto.request.group.CreateGroupRequestDto;
import com.app.gamereview.dto.request.group.GetAllGroupsFilterRequestDto;
import com.app.gamereview.dto.request.group.RemoveGroupTagRequestDto;
import com.app.gamereview.dto.response.tag.AddGroupTagResponseDto;
import com.app.gamereview.model.Group;
import com.app.gamereview.model.User;
import com.app.gamereview.service.GroupService;
import com.app.gamereview.util.validation.annotation.AuthorizationRequired;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

	@AuthorizationRequired
	@PostMapping("/add-tag")
	public ResponseEntity<AddGroupTagResponseDto> addGroupTag(
			@Valid @RequestBody AddGroupTagRequestDto addGroupTagRequestDto) {
		AddGroupTagResponseDto response = groupService.addGroupTag(addGroupTagRequestDto);
		return ResponseEntity.ok(response);
	}

	@AuthorizationRequired
	@DeleteMapping("/remove-tag")
	public ResponseEntity<Boolean> removeGroupTag(
			@Valid @RequestBody RemoveGroupTagRequestDto removeGroupTagRequestDto) {
		Boolean response = groupService.removeGroupTag(removeGroupTagRequestDto);
		return ResponseEntity.ok(response);
	}

	@AuthorizationRequired
	@PostMapping("/join")
	public ResponseEntity<Boolean> joinGroup(@RequestParam String id,
									  @RequestHeader String Authorization, HttpServletRequest request) {
		User user = (User) request.getAttribute("authenticatedUser");
		Boolean joined = groupService.joinGroup(id, user);
		return ResponseEntity.ok(joined);
	}

	@AuthorizationRequired
	@PostMapping("/leave")
	public ResponseEntity<Boolean> leaveGroup(@RequestParam String id,
											 @RequestHeader String Authorization, HttpServletRequest request) {
		User user = (User) request.getAttribute("authenticatedUser");
		Boolean leave = groupService.leaveGroup(id, user);
		return ResponseEntity.ok(leave);
	}
}
