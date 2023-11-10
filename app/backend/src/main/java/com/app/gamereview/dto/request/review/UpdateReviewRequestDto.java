package com.app.gamereview.dto.request.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateReviewRequestDto {

    @Size(max = 150, message = "Description must be at most 150 characters long")
    private String reviewDescription;

    @Max(value=5, message = "Rating cannot be more than 5")
    @Min(value=0, message = "Rating cannot be less than 0")
    private float rating;
}
