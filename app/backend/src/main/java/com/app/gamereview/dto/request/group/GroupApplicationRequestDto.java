package com.app.gamereview.dto.request.group;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GroupApplicationRequestDto {

    @NotEmpty(message = "Application message cannot be null or empty")
    @Size(min = 10, message = "Application message must be at least 10 characters long")
    @Size(max = 300, message = "Application message must be at most 300 characters long")
    private String message;
}
