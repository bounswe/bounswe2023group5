package com.app.gamereview.dto.response.auth;

import com.app.gamereview.dto.response.user.UserResponseDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginUserResponseDto {

	public UserResponseDto user;

	public String token;



}


