package com.app.gamereview.controller;

import com.app.gamereview.dto.request.review.GetAllReviewsFilterRequestDto;
import com.app.gamereview.dto.response.profile.GetLastActivitiesResponseDto;
import com.app.gamereview.dto.response.review.GetAllReviewsResponseDto;
import com.app.gamereview.model.User;
import com.app.gamereview.service.PostService;
import com.app.gamereview.service.ProfileService;
import com.app.gamereview.util.JwtUtil;
import com.app.gamereview.util.validation.annotation.AuthorizationRequired;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<GetLastActivitiesResponseDto>> getReviews(@RequestHeader String Authorization, HttpServletRequest request)  {

        User user = (User) request.getAttribute("authenticatedUser");

        List<GetLastActivitiesResponseDto> reviews = profileService.getLastActivities(user);
        return ResponseEntity.ok(reviews);
    }
}
