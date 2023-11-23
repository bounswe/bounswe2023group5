package com.app.gamereview.dto.request.achievement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAchievementRequestDto {

    private String title;

    private String description;

    private String icon;
}
