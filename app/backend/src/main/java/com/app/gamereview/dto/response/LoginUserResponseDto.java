package com.app.gamereview.dto.response;

import com.app.gamereview.model.User;

public class LoginUserResponseDto {
    public User user;
    public String token;

    public void setUser(User user) {
        this.user = user;
    }

    public void setToken(String token) {
        this.token = token;
    }
}



