package com.app.gamereview.controller;

import com.app.gamereview.dto.request.user.GetAllUsersFilterRequestDto;
import com.app.gamereview.model.User;
import com.app.gamereview.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/get-all")
	public ResponseEntity<List<User>> getUsers(GetAllUsersFilterRequestDto filter) {
		List<User> users = userService.getAllUsers(filter);
		return ResponseEntity.ok(users);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Boolean> deleteUser(@RequestParam String id) {
		Boolean deleteResult = userService.deleteUserById(id);

		return ResponseEntity.ok(deleteResult);
	}

}
