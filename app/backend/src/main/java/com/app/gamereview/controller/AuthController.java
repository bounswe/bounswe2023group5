package com.app.gamereview.controller;

import com.app.gamereview.dto.request.LoginUserRequestDto;
import com.app.gamereview.dto.request.ChangeUserPasswordRequestDto;
import com.app.gamereview.dto.request.RegisterUserRequestDto;
import com.app.gamereview.dto.response.LoginUserResponseDto;
import com.app.gamereview.model.ResetCode;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.ResetCodeRepository;
import com.app.gamereview.service.AuthService;
import com.app.gamereview.service.EmailService;
import com.app.gamereview.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;
    private final UserService userService;
    private final ResetCodeRepository resetCodeRepository;

    @Autowired
    public AuthController(AuthService authService, EmailService emailService, UserService userService, ResetCodeRepository resetCodeRepository) {
        this.authService = authService;
        this.emailService= emailService;
        this.userService= userService;
        this.resetCodeRepository = resetCodeRepository;
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

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        User user = userService.getUserByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Generate and save a reset code (you can use UUID or any secure method)
        String code = generateResetCode(user.getId());

        // Send email with reset code
        String subject = "Password Reset";
        String message = "Your password reset code is: " + code;
        message += "\n The reset code will expire after 24 hours.";
        emailService.sendEmail(email, subject, message);

        return ResponseEntity.ok("Reset code sent successfully");
    }
    private String generateResetCode(String userId) {
        // Check if a reset code exists for the user
        ResetCode existingResetCode = resetCodeRepository.findByUserId(userId);

        // If a reset code exists, delete it
        if (existingResetCode != null) {
            resetCodeRepository.delete(existingResetCode);
        }
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();

        ResetCode resetCode = new ResetCode(code, userId, new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
        resetCodeRepository.save(resetCode);

        return code;
    }
}
