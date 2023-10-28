package com.app.gamereview.dto.response;

import lombok.Getter;

@Getter
public class GetGameListResponseDto {

	private String gameName;

	private String gameDescription;

	private String gameIcon;

	public GetGameListResponseDto(String gameName, String gameDescription, String gameIcon) {
		this.gameName = gameName;
		this.gameDescription = gameDescription;
		this.gameIcon = gameIcon;
	}

	// TODO add tag dependent fields and add to constructor

}
