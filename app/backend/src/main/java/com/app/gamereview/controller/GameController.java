package com.app.gamereview.controller;

import com.app.gamereview.dto.request.game.*;
import com.app.gamereview.dto.response.game.GameDetailResponseDto;
import com.app.gamereview.dto.response.game.GetGameListResponseDto;
import com.app.gamereview.dto.response.tag.AddGameTagResponseDto;
import com.app.gamereview.dto.response.tag.GetAllTagsOfGameResponseDto;
import com.app.gamereview.model.Game;
import com.app.gamereview.service.GameService;
import com.app.gamereview.util.validation.annotation.AdminRequired;
import com.app.gamereview.util.validation.annotation.AuthorizationRequired;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game")
@Validated
public class GameController {

	private final GameService gameService;

	@Autowired
	public GameController(GameService gameService) {
		this.gameService = gameService;
	}

	@PostMapping("get-game-list")
	public ResponseEntity<List<GetGameListResponseDto>> getAllGames(@RequestBody(required = false) GetGameListRequestDto filter) {

		List<GetGameListResponseDto> gameList = gameService.getAllGames(filter);
		return ResponseEntity.ok().body(gameList);
	}

	@GetMapping("get-game-list")
	public ResponseEntity<List<Game>> getGames(@ParameterObject GetGameListRequestDto filter) {
		List<Game> gameList = gameService.getGames(filter);
		return ResponseEntity.ok(gameList);
	}

	@GetMapping("game-by-name")
	public ResponseEntity<GameDetailResponseDto> getGameByName(String name) {
		GameDetailResponseDto game = gameService.getGameByName(name);
		return ResponseEntity.ok(game);
	}

	@AuthorizationRequired
	@AdminRequired
	@PostMapping("/add-tag")
	public ResponseEntity<AddGameTagResponseDto> addGameTag(
			@Valid @RequestBody AddGameTagRequestDto addGameTagRequestDto) {
		AddGameTagResponseDto response = gameService.addGameTag(addGameTagRequestDto);
		return ResponseEntity.ok(response);
	}

	@AuthorizationRequired
	@AdminRequired
	@DeleteMapping("/remove-tag")
	public ResponseEntity<Boolean> removeGameTag(
			@Valid @RequestBody RemoveGameTagRequestDto removeGameTagRequestDto) {
		Boolean response = gameService.removeGameTag(removeGameTagRequestDto);
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

	@AuthorizationRequired
	@PutMapping("/update")
	public ResponseEntity<Game> editGame(@RequestParam String id,
										   @Valid @RequestBody UpdateGameRequestDto updateGameRequestDto,
										   @RequestHeader String Authorization, HttpServletRequest request) {
		Game updatedGame = gameService.editGame(id, updateGameRequestDto);
		return ResponseEntity.ok(updatedGame);
	}

	@AuthorizationRequired
	@AdminRequired
	@DeleteMapping("/delete")
	public ResponseEntity<Boolean> deleteGame(@RequestParam String id, @RequestHeader String Authorization,
											  HttpServletRequest request) {
		Boolean isDeleted = gameService.deleteGame(id);
		return ResponseEntity.ok(isDeleted);
	}
}
