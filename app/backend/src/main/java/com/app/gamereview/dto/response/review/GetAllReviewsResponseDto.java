package com.app.gamereview.dto.response.review;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetAllReviewsResponseDto {

    private String id;

    private String reviewDescription;

    private float rating;

    private String gameId;

    private String reviewedUser;   // user username

    private int overallVote;

    private int reportNum;

    private LocalDateTime createdAt;
}
