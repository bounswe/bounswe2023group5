package com.app.gamereview.controller;

import com.app.gamereview.dto.request.tag.AddGameTagRequestDto;
import com.app.gamereview.dto.response.tag.AddGameTagResponseDto;
import com.app.gamereview.dto.response.tag.GetAllTagsOfGameResponseDto;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.gamereview.dto.request.GetGameListRequestDto;
import com.app.gamereview.dto.response.GetGameListResponseDto;
import com.app.gamereview.service.GameService;

import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameController {

	private final GameService gameService;

	@Autowired
	public GameController(GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping("get-game-list")
	public ResponseEntity<List<GetGameListResponseDto>> getGames(GetGameListRequestDto filter) {
		List<GetGameListResponseDto> gameList = gameService.getAllGames(filter);
		return ResponseEntity.ok().body(gameList);
	}

	@PostMapping("/add-tag")
	public ResponseEntity<AddGameTagResponseDto> addGameTag(AddGameTagRequestDto addGameTagRequestDto) {
		AddGameTagResponseDto response = gameService.addGameTag(addGameTagRequestDto);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/get-all-tags")
	public ResponseEntity<GetAllTagsOfGameResponseDto> getAllTags(@RequestParam String gameId){
		GetAllTagsOfGameResponseDto response = gameService.getGameTags(gameId);
		return ResponseEntity.ok(response);
	}
}
