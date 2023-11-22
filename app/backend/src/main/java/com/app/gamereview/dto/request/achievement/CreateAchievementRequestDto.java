package com.app.gamereview.dto.request.achievement;

import com.app.gamereview.enums.AchievementType;
import com.app.gamereview.util.validation.annotation.ValidAchievementType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAchievementRequestDto {

    @NotEmpty(message = "Achievement title cannot be empty.")
    private String title;

    @NotEmpty(message = "Achievement description cannot be empty.")
    private String description;

    @NotEmpty(message = "Achievement icon cannot be empty.")
    private String icon;

    @ValidAchievementType(allowedValues = {AchievementType.GAME, AchievementType.META})
    @NotEmpty(message = "Achievement type cannot be empty.")
    private AchievementType type;

    @Pattern(regexp = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$",
            message = "Game has invalid Id (UUID) format")
    private String gameId;
}
