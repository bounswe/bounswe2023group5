package com.app.gamereview.controller;


import com.app.gamereview.dto.request.RegisterUserRequestDto;
import com.app.gamereview.model.User;
import com.app.gamereview.service.AuthService;
import com.app.gamereview.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    private EmailService emailService;


    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegisterUserRequestDto registerUserRequestDto){
        User userToCreate = authService.registerUser(registerUserRequestDto);
        return ResponseEntity.ok(userToCreate);
    }

    @PostMapping("/send-email")
    public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String body) {
        emailService.sendEmail(to, subject, body);
        return "Email sent successfully!";
    }
}
