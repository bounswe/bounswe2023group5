package com.app.gamereview.dto.request.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetGameListRequestDto {

	private Boolean findDeleted;

	private List<String> playerTypes;

	private List<String> genre;

	private String production;

	private List<String> platform;

	private List<String> artStyle;

	private String search;

}
