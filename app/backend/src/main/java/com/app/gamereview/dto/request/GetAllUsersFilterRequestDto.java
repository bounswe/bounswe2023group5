package com.app.gamereview.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllUsersFilterRequestDto {

	private String id;

	private String username;

	private Boolean isDeleted;

}
