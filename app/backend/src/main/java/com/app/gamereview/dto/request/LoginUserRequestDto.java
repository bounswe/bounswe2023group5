package com.app.gamereview.dto.request;

public class LoginUserRequestDto {
    public String password;
    public String email;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
