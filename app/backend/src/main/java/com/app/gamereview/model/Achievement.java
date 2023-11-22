package com.app.gamereview.model;

import com.app.gamereview.enums.AchievementType;
import com.app.gamereview.model.common.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Achievement")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Achievement extends BaseModel {

    private String title;

    private String description;

    private String icon;

    private AchievementType type;

    private String game;
}
