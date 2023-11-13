package com.app.gamereview.dto.request.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllReviewsFilterRequestDto {
    private String gameId;

    private String reviewedBy;

    private Boolean withDeleted = false;
}
