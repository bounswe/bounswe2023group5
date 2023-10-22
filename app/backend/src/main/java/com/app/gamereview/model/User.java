package com.app.gamereview.model;

import com.app.gamereview.model.common.BaseModel;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
@TypeAlias("User")
public class User extends BaseModel {

    public String username;

    public String password;

    public String email;

    public String role;

    public Boolean isVerified;


    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}