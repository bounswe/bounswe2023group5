package com.app.gamereview.controller;


import com.app.gamereview.dto.GetAllUsersFilterDto;
import com.app.gamereview.dto.request.RegisterUserRequestDto;
import com.app.gamereview.model.User;
import com.app.gamereview.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(value = "username", required = false) final String username,
            @RequestParam(value = "isDeleted", required = false) final Boolean isDeleted){

        GetAllUsersFilterDto filter = new GetAllUsersFilterDto();
        filter.username = username;
        filter.isDeleted = isDeleted;

        List<User> users = userService.getAllUsers(filter);
        return ResponseEntity.ok(users);
    }

}
