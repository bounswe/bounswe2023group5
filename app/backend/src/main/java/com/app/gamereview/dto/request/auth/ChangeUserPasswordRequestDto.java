package com.app.gamereview.dto.request.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ChangeUserPasswordRequestDto {
	
	@NotNull(message = "Provided current password cannot be null or empty")
	private String currentPassword;

	@NotNull(message = "Provided new password cannot be null or empty")
	@Size(min = 6, message = "New password must be at least 6 characters long")
	@Size(max = 24, message = "New password must be at most 24 characters long")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
			message = "New password must have at least one uppercase letter, one lowercase letter, and one digit")
	private String newPassword;

}
