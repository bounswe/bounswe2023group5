package com.app.gamereview.controller;

import com.app.gamereview.dto.request.LoginUserRequestDto;
import com.app.gamereview.dto.request.ChangeUserPasswordRequestDto;
import com.app.gamereview.dto.request.ForgotChangeUserPasswordRequestDto;
import com.app.gamereview.dto.request.ForgotPasswordRequestDto;
import com.app.gamereview.dto.request.RegisterUserRequestDto;
import com.app.gamereview.dto.request.VerifyResetCodeRequestDto;
import com.app.gamereview.dto.request.*;
import com.app.gamereview.dto.response.LoginUserResponseDto;
import com.app.gamereview.dto.response.UserResponseDto;
import com.app.gamereview.model.ResetCode;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.ResetCodeRepository;
import com.app.gamereview.service.AuthService;
import com.app.gamereview.service.EmailService;
import com.app.gamereview.service.UserService;
import com.app.gamereview.util.AuthorizationRequired;
import com.app.gamereview.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	private final EmailService emailService;

	private final UserService userService;

	@Autowired
	public AuthController(AuthService authService, EmailService emailService, UserService userService) {
		this.authService = authService;
		this.emailService = emailService;
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody RegisterUserRequestDto registerUserRequestDto) {
		User userToCreate = authService.registerUser(registerUserRequestDto);
		return ResponseEntity.ok(userToCreate);
	}

	@AuthorizationRequired
	@PostMapping("/change-password")
	public ResponseEntity<Boolean> changePassword(@RequestBody ChangeUserPasswordRequestDto passwordRequestDto,
			@RequestHeader String Authorization, HttpServletRequest request) {
		User user = (User) request.getAttribute("authenticatedUser");
		Boolean changePasswordResult = authService.changeUserPassword(passwordRequestDto, user);
		return ResponseEntity.ok(changePasswordResult);
	}

	@AuthorizationRequired
	@PostMapping("/change-forgot-password")
	public ResponseEntity<Boolean> changeForgotPassword(
			@RequestBody ForgotChangeUserPasswordRequestDto passwordRequestDto, @RequestHeader String Authorization,
			HttpServletRequest request) {
		User user = (User) request.getAttribute("authenticatedUser");
		Boolean changePasswordResult = authService.changeForgotPassword(passwordRequestDto, user);
		return ResponseEntity.ok(changePasswordResult);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginUserResponseDto> login(@RequestBody LoginUserRequestDto loginRequest) {
		LoginUserResponseDto loginResponse = authService.loginUser(loginRequest);
		return ResponseEntity.ok(loginResponse);
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDto forgotRequest) {
		String email = forgotRequest.getEmail();
		User user = userService.getUserByEmail(email);

		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}

		// Generate and save a reset code (you can use UUID or any secure method)
		String code = authService.generateResetCode(user.getId());

		// Send email with reset code
		String subject = "Password Reset";
		String message = "Your password reset code is: " + code;
		message += "\n The reset code will expire after 24 hours.";
		emailService.sendEmail(email, subject, message);

		return ResponseEntity.ok().body("Reset code sent successfully");
	}

	@PostMapping("/verify-reset-code")
	public ResponseEntity<String> verifyResetCode(@RequestBody VerifyResetCodeRequestDto request) {
		ResetCode resetCode = authService.getResetCodeByCode(request.getResetCode());
		if (resetCode == null || resetCode.getExpirationDate().before(new Date())) {
			// Invalid or expired reset code
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid or expired reset code");
		}

		// Check if the reset code matches the user
		String userEmail = userService.getUserById(resetCode.getUserId()).getEmail();
		if (!userEmail.equals(request.getUserEmail())) {
			// Reset code does not match the user
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email and Code doesn't match");
		}

		// Reset code is valid, generate a JWT token for the user
		String token = JwtUtil.generateToken(userService.getUserById(resetCode.getUserId()).getEmail());

		// Clear the reset code after generating the token
		authService.deleteCodeByUserId(resetCode.getUserId());

		return ResponseEntity.ok().body(token);
	}

	@AuthorizationRequired
	@PostMapping("/me")
	public ResponseEntity<UserResponseDto> me(@RequestHeader String Authorization, HttpServletRequest request) {
		User user = (User) request.getAttribute("authenticatedUser");
		UserResponseDto userResponse = authService.me(user);
		return ResponseEntity.ok(userResponse);
	}

}
