package com.app.gamereview.controller;

import com.app.gamereview.dto.request.game.CreateGameRequestDto;
import com.app.gamereview.dto.request.tag.AddGameTagRequestDto;
import com.app.gamereview.dto.response.game.GameDetailResponseDto;
import com.app.gamereview.dto.response.tag.AddGameTagResponseDto;
import com.app.gamereview.dto.response.tag.GetAllTagsOfGameResponseDto;
import com.app.gamereview.model.Game;
import com.app.gamereview.service.FileStorageService;
import com.app.gamereview.util.validation.annotation.AdminRequired;
import com.app.gamereview.util.validation.annotation.AuthorizationRequired;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.app.gamereview.dto.request.game.GetGameListRequestDto;
import com.app.gamereview.dto.response.game.GetGameListResponseDto;
import com.app.gamereview.service.GameService;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/game")
@Validated
public class GameController {

	private final GameService gameService;

	private final FileStorageService fileService;

	@Autowired
	public GameController(GameService gameService, FileStorageService fileService) {
		this.gameService = gameService;
		this.fileService = fileService;
	}

	@PostMapping("get-game-list")
	public ResponseEntity<List<GetGameListResponseDto>> getGames(@RequestBody(required = false) GetGameListRequestDto filter) {

		List<GetGameListResponseDto> gameList = gameService.getAllGames(filter);
		return ResponseEntity.ok().body(gameList);
	}

	@AuthorizationRequired
	@AdminRequired
	@PostMapping("/add-tag")
	public ResponseEntity<AddGameTagResponseDto> addGameTag(
			@Valid @RequestBody AddGameTagRequestDto addGameTagRequestDto) {
		AddGameTagResponseDto response = gameService.addGameTag(addGameTagRequestDto);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/get-all-tags")
	public ResponseEntity<GetAllTagsOfGameResponseDto> getAllTags(@RequestParam String gameId){
		GetAllTagsOfGameResponseDto response = gameService.getGameTags(gameId);
		return ResponseEntity.ok(response);
	}
	@GetMapping("/get-game")
	public ResponseEntity<GameDetailResponseDto> getGameDetail(@RequestParam String gameId){
		GameDetailResponseDto response = gameService.getGameDetail(gameId);
		return ResponseEntity.ok(response);
	}

	@AuthorizationRequired
	@PostMapping("/create")
	public ResponseEntity<Game> createGame(@Valid @RequestBody CreateGameRequestDto createGameRequestDto,
										   @RequestHeader String Authorization) {
		Game gameToCreate = gameService.createGame(createGameRequestDto);
		return ResponseEntity.ok(gameToCreate);
	}
}
