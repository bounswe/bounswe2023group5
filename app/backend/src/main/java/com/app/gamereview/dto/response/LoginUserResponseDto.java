package com.app.gamereview.dto.response;

import com.app.gamereview.model.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginUserResponseDto {

	public UserResponseDto user;

	public String token;



}


