package com.app.gamereview.dto.response.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetGameListResponseDto {

	private String id;

	private String gameName;

	private String gameDescription;

	private String gameIcon;

	// TODO add tag dependent fields and add to constructor

}
