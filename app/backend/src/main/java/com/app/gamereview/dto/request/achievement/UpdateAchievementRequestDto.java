package com.app.gamereview.dto.request.achievement;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAchievementRequestDto {

    @NotBlank(message = "Game title cannot be blank.")
    private String title;

    @NotBlank(message = "Game description cannot be blank.")
    private String description;

    @NotBlank(message = "Game icon cannot be blank.")
    private String icon;
}
