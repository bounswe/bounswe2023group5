package com.app.gamereview.dto.request;

import lombok.Getter;

@Getter
public class ForgotChangeUserPasswordRequestDto {

	private String userId;

	private String newPassword;

}
