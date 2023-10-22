package com.app.gamereview.dto.request;

import lombok.Getter;

@Getter
public class VerifyResetCodeRequestDto {

    private String resetCode;

    private String userEmail;

}