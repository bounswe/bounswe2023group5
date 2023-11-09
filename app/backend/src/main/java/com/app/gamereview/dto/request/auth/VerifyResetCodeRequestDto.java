package com.app.gamereview.dto.request.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifyResetCodeRequestDto {

	@NotEmpty(message = "Reset code field cannot be null or empty")
	private String resetCode;

	@NotEmpty(message = "Email field cannot be null or empty")
	private String userEmail;

}