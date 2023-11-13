package com.app.gamereview.dto.response.user;

import com.app.gamereview.enums.UserRole;
import com.app.gamereview.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

	private String username;

	private String id;

	private String email;

	private UserRole role;

	private Boolean isVerified;

	public UserResponseDto(User user) {
		this.email = user.getEmail();
		this.username = user.getUsername();
		this.role = user.getRole();
		this.isVerified = user.getVerified();
		this.id = user.getId();
	}

}
