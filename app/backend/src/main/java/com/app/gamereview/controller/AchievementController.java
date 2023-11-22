package com.app.gamereview.controller;

import com.app.gamereview.dto.request.achievement.CreateAchievementRequestDto;
import com.app.gamereview.dto.request.achievement.GrantAchievementRequestDto;
import com.app.gamereview.dto.request.achievement.UpdateAchievementRequestDto;
import com.app.gamereview.service.AchievementService;
import com.app.gamereview.util.validation.annotation.AdminRequired;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.app.gamereview.model.Achievement;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/achievement")
@Validated
public class AchievementController {

    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @PostMapping("/create")
    @AdminRequired
    public ResponseEntity<Achievement> createAchievement(@Valid @RequestBody CreateAchievementRequestDto achievementDto,
                                                         @RequestHeader String Authorization, HttpServletRequest request) {
        Achievement achievement = achievementService.createAchievement(achievementDto);
        return ResponseEntity.ok(achievement);
    }

    @PostMapping("/update")
    @AdminRequired
    public ResponseEntity<Achievement> updateAchievement(@RequestParam String id,
                                                         @Valid @RequestBody UpdateAchievementRequestDto updateAchievementRequestDto,
                                                         @RequestHeader String Authorization, HttpServletRequest request) {
        Achievement achievement = achievementService.updateAchievement(id, updateAchievementRequestDto);
        return ResponseEntity.ok(achievement);
    }

    @DeleteMapping("/delete")
    @AdminRequired
    public ResponseEntity<Achievement> deleteAchievement(@RequestParam String id, @RequestHeader String Authorization,
                                                         HttpServletRequest request) {
        Achievement achievement = achievementService.deleteAchievement(id);
        return ResponseEntity.ok(achievement);
    }

    //TODO
    @GetMapping("/get-game-achievements")
    public ResponseEntity<List<Achievement>> getGameAchievements() {
        return ResponseEntity.ok(new ArrayList<Achievement>());
    }

    //TODO
    @PostMapping("/grant-achievement")
    @AdminRequired
    public ResponseEntity<List<String>> grantAchievement(@Valid @RequestBody GrantAchievementRequestDto achievementDto,
                                                        @RequestHeader String Authorization, HttpServletRequest request) {
        List<String> userAchievements = achievementService.grantAchievement(achievementDto);
        return ResponseEntity.ok(userAchievements);
    }
}
