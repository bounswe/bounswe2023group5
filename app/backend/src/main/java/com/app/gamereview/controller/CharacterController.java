package com.app.gamereview.controller;

import com.app.gamereview.dto.request.character.CreateCharacterRequestDto;
import com.app.gamereview.dto.request.character.UpdateCharacterRequestDto;
import com.app.gamereview.model.Character;
import com.app.gamereview.service.CharacterService;
import com.app.gamereview.util.validation.annotation.AdminRequired;
import com.app.gamereview.util.validation.annotation.AuthorizationRequired;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/character")
@Validated
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @PostMapping("/create")
    @AuthorizationRequired
    @AdminRequired
    public ResponseEntity<Character> createCharacter(@Valid @RequestBody CreateCharacterRequestDto characterDto,
                                                       @RequestHeader String Authorization, HttpServletRequest request) {
        Character character = characterService.createCharacter(characterDto);
        return ResponseEntity.ok(character);
    }

    @PostMapping("/update")
    @AuthorizationRequired
    @AdminRequired
    public ResponseEntity<Character> updateCharacter(@RequestParam String id,
                                                         @Valid @RequestBody UpdateCharacterRequestDto updateCharacterRequestDto,
                                                         @RequestHeader String Authorization, HttpServletRequest request) {
        Character character = characterService.updateCharacter(id, updateCharacterRequestDto);
        return ResponseEntity.ok(character);
    }

    @DeleteMapping("/delete")
    @AuthorizationRequired
    @AdminRequired
    public ResponseEntity<Character> deleteCharacter(@RequestParam String id, @RequestHeader String Authorization,
                                                         HttpServletRequest request) {
        Character character = characterService.deleteCharacter(id);
        return ResponseEntity.ok(character);
    }

    @GetMapping("/get-game-characters")
    public ResponseEntity<List<Character>> getGameCharacters(@RequestParam String gameId) {
        List<Character> gameCharacters = characterService.getGameCharacters(gameId);
        return ResponseEntity.ok(gameCharacters);
    }
}
