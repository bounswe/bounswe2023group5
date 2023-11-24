package com.app.gamereview.controller;

import com.app.gamereview.dto.request.group.*;
import com.app.gamereview.dto.response.group.GetGroupResponseDto;
import com.app.gamereview.dto.response.tag.AddGroupTagResponseDto;
import com.app.gamereview.model.Group;
import com.app.gamereview.model.User;
import com.app.gamereview.service.GroupService;
import com.app.gamereview.util.JwtUtil;
import com.app.gamereview.util.validation.annotation.AdminRequired;
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
@RequestMapping("/api/group")
@Validated
public class GroupController {

	private final GroupService groupService;

	@Autowired
	public GroupController(
			GroupService groupService
	) {
		this.groupService = groupService;
	}

	@GetMapping("/get-all")
	public ResponseEntity<List<GetGroupResponseDto>> getReviews(
			@ParameterObject GetAllGroupsFilterRequestDto filter, @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String Authorization) {

		String email;
        if (JwtUtil.validateToken(Authorization)) email = JwtUtil.extractSubject(Authorization);
        else email = "";

        List<GetGroupResponseDto> groups = groupService.getAllGroups(filter, email);
		return ResponseEntity.ok(groups);
	}

	@GetMapping("/get")
	public ResponseEntity<GetGroupResponseDto> getGroup(
			@RequestParam String id, @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String Authorization) {

		String email;
		if (JwtUtil.validateToken(Authorization)) email = JwtUtil.extractSubject(Authorization);
		else email = "";

		GetGroupResponseDto group = groupService.getGroupById(id, email);

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
	@PutMapping("/update")
	public ResponseEntity<Group> editGroup(@RequestParam String id,
										   @Valid @RequestBody UpdateGroupRequestDto updateGroupRequestDto,
										   @RequestHeader String Authorization, HttpServletRequest request) {
		Group updatedGroup = groupService.updateGroup(id,updateGroupRequestDto);
		return ResponseEntity.ok(updatedGroup);
	}

	@AuthorizationRequired
	@DeleteMapping("/delete")
	public ResponseEntity<Boolean> deleteGroup(String identifier,
											 @RequestHeader String Authorization, HttpServletRequest request) {
		User user = (User) request.getAttribute("authenticatedUser");
		Boolean response = groupService.deleteGroup(identifier);
		return ResponseEntity.ok(response);
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

	@AuthorizationRequired
	@PutMapping("/ban-user")
	public ResponseEntity<Boolean> banUser(@RequestParam String groupId, @RequestParam String userId, @RequestHeader String Authorization, HttpServletRequest request) {
		User user = (User) request.getAttribute("authenticatedUser");
		Boolean result = groupService.banUser(groupId, userId, user);
		return ResponseEntity.ok(result);
	}
	@AuthorizationRequired
	@PutMapping("/add-moderator")
	public ResponseEntity<Boolean> addModerator(@RequestParam String groupId, @RequestParam String userId, @RequestHeader String Authorization, HttpServletRequest request) {
		User user = (User) request.getAttribute("authenticatedUser");
		Boolean result = groupService.addModerator(groupId, userId, user);
		return ResponseEntity.ok(result);
	}

	@AuthorizationRequired
	@AdminRequired
	@PutMapping("/remove-moderator")
	public ResponseEntity<Boolean> removeModerator(@RequestParam String groupId, @RequestParam String userId, @RequestHeader String Authorization, HttpServletRequest request) {
		Boolean result = groupService.removeModerator(groupId, userId);
		return ResponseEntity.ok(result);
	}

	@AuthorizationRequired
	@PutMapping("/unban-user")
	public ResponseEntity<Boolean> unbanUser(@RequestParam String groupId, @RequestParam String userId, @RequestHeader String Authorization, HttpServletRequest request) {
		User user = (User) request.getAttribute("authenticatedUser");
		Boolean result = groupService.unbanUser(groupId, userId, user);
		return ResponseEntity.ok(result);
	}
}
