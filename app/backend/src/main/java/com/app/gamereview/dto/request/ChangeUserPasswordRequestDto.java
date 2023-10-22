package com.app.gamereview.dto.request;

import lombok.Getter;

@Getter
public class ChangeUserPasswordRequestDto {

	private String userId;

	private String currentPassword;

	private String newPassword;

}
