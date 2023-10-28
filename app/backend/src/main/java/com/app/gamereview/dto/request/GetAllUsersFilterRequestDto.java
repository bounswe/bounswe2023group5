package com.app.gamereview.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllUsersFilterRequestDto {

	private String id;

	private String username;

	private Boolean isDeleted;

	// public String getId() {
	// return id;
	// }

	// public void setId(String id) {
	// this.id = id;
	// }

	// public String getUsername() {
	// return username;
	// }

	// public void setUsername(String username) {
	// this.username = username;
	// }

	// public Boolean getDeleted() {
	// return isDeleted;
	// }

	// public void setDeleted(Boolean isDeleted) {
	// this.isDeleted = isDeleted;
	// }

}
