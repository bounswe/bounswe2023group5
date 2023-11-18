package com.app.gamereview.dto.request.review;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewRequestDto {

    @Size(max = 300, message = "Description must be at most 300 characters long")
    private String reviewDescription = null;

    @Max(value=5, message = "Rating cannot be more than 5")
    @Min(value=0, message = "Rating cannot be less than 0")
    private float rating;

    @NotEmpty(message = "Game Id field cannot be null or empty")
    @Pattern(regexp = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$",
            message = "Game Id has Invalid Id (UUID) format")
    private String gameId;
}
