package com.app.gamereview.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifyResetCodeRequestDto {

	private String resetCode;

	private String userEmail;

}