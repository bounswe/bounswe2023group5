package com.app.gamereview.model;

import com.app.gamereview.enums.UserRole;
import com.app.gamereview.model.common.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "User")
@TypeAlias("User")
@Getter
@Setter
public class User extends BaseModel {

	private String username;

	private String password;

	private String email;

	private UserRole role;

	private Boolean isVerified;

	private List<String> achievements = new ArrayList<>();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public Boolean getVerified() {
		return isVerified;
	}

	public void setVerified(Boolean verified) {
		isVerified = verified;
	}

}
