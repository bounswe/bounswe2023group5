package com.app.gamereview.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;



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
    public ResponseEntity<List<GetGameListResponseDto>> getGames(@RequestBody GetGameListRequestDto filter) {
        List<GetGameListResponseDto> gameList = gameService.getAllGames(filter);
        return ResponseEntity.ok().body(gameList);
    }
    
}
