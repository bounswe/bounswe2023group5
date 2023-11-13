package com.app.gamereview.dto.request.auth;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequestDto {

	@NotEmpty(message = "Username field cannot be null or empty")
	@Size(min = 3, message = "Username must be at least 3 characters long")
	@Size(max = 15, message = "Username must be at most 15 characters long")
	private String username;

	@NotNull(message = "Password cannot be null or empty")
	@Size(min = 6, message = "Password must be at least 6 characters long")
	@Size(max = 24, message = "Password must be at most 24 characters long")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
			message = "Password must have at least one uppercase letter, one lowercase letter, and one digit")
	private String password;

	@Email(message = "Provided email format is not valid")
	private String email;

}
