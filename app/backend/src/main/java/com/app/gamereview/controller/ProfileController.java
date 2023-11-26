package com.app.gamereview.controller;

import com.app.gamereview.dto.request.profile.EditProfileRequestDto;
import com.app.gamereview.dto.request.profile.ProfileUpdateGameRequestDto;
import com.app.gamereview.dto.response.profile.GetLastActivitiesResponseDto;
import com.app.gamereview.dto.response.profile.ProfilePageResponseDto;
import com.app.gamereview.model.Profile;
import com.app.gamereview.model.User;
import com.app.gamereview.service.ProfileService;
import com.app.gamereview.util.JwtUtil;
import com.app.gamereview.util.validation.annotation.AuthorizationRequired;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@Validated
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @AuthorizationRequired
    @GetMapping("/get-activites")
    public ResponseEntity<List<GetLastActivitiesResponseDto>> getReviews(@RequestHeader String Authorization, HttpServletRequest request) {

        User user = (User) request.getAttribute("authenticatedUser");

        List<GetLastActivitiesResponseDto> reviews = profileService.getLastActivities(user);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/get")
    public ResponseEntity<ProfilePageResponseDto> getProfile(@RequestParam String userId, @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String Authorization) {
        String email;
        if (JwtUtil.validateToken(Authorization)) email = JwtUtil.extractSubject(Authorization);
        else email = "";

        ProfilePageResponseDto profile = profileService.getProfile(userId, email);
        return ResponseEntity.ok(profile);
    }

    @AuthorizationRequired
    @PostMapping("/edit")
    public ResponseEntity<Profile> editProfile(@RequestParam String id, @Valid @RequestBody EditProfileRequestDto profile, @RequestHeader String Authorization, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        Profile editedProfile = profileService.editProfile(id, profile, user);
        return ResponseEntity.ok(editedProfile);
    }

    @AuthorizationRequired
    @PostMapping("/add-game")
    public ResponseEntity<Profile> addGameToProfile(@RequestParam String id, @Valid @RequestBody ProfileUpdateGameRequestDto dto, @RequestHeader String Authorization, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        Profile editedProfile = profileService.addGameToProfile(id, dto, user);
        return ResponseEntity.ok(editedProfile);
    }

    @AuthorizationRequired
    @PostMapping("/remove-game")
    public ResponseEntity<Profile> removeGameFromProfile(@RequestParam String id, @Valid @RequestBody ProfileUpdateGameRequestDto dto, @RequestHeader String Authorization, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        Profile editedProfile = profileService.removeGameFromProfile(id, dto, user);
        return ResponseEntity.ok(editedProfile);
    }

}
