package com.app.gamereview.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ForgotPasswordRequestDto {

	@Email(message = "Provided email address is not valid")
	private String email;

}
