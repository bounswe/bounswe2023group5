package com.app.gamereview.controller;

import com.app.gamereview.dto.request.LoginUserRequestDto;
import com.app.gamereview.dto.request.ChangeUserPasswordRequestDto;
import com.app.gamereview.dto.request.RegisterUserRequestDto;
import com.app.gamereview.dto.response.LoginUserResponseDto;
import com.app.gamereview.model.User;
import com.app.gamereview.service.AuthService;
import com.app.gamereview.service.EmailService;,
import com.app.gamereview.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    @Autowired
    public AuthController(AuthService authService, EmailService emailService) {
        this.authService = authService;
        this.emailService= emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegisterUserRequestDto registerUserRequestDto){
        User userToCreate = authService.registerUser(registerUserRequestDto);
        return ResponseEntity.ok(userToCreate);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangeUserPasswordRequestDto passwordRequestDto) {
        Boolean changePasswordResult = authService.changeUserPassword(passwordRequestDto);
        return ResponseEntity.ok(changePasswordResult);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponseDto> login(@RequestBody LoginUserRequestDto loginRequest) {
        LoginUserResponseDto loginResponse = authService.loginUser(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/send-email")
    public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String body) {
        emailService.sendEmail(to, subject, body);
        return "Email sent successfully!";
    }
}
