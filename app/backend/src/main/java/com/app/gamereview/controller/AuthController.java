package com.app.gamereview.controller;


import com.app.gamereview.dto.request.RegisterUserRequestDto;
import com.app.gamereview.model.User;
import com.app.gamereview.service.AuthService;
import com.app.gamereview.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegisterUserRequestDto registerUserRequestDto){
        User userToCreate = authService.registerUser(registerUserRequestDto);
        return ResponseEntity.ok(userToCreate);
    }
}
