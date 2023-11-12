package com.app.gamereview.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserRequestDto {

	@NotEmpty(message = "Password field cannot be null or empty")
	public String password;

	@Email(message = "Provided email format is not valid")
	public String email;

}
