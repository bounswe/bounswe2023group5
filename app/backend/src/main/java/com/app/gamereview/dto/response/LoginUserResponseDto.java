package com.app.gamereview.dto.response;

import com.app.gamereview.model.User;
import lombok.Setter;
@Setter
public class LoginUserResponseDto {
    public User user;
    public String token;

}



