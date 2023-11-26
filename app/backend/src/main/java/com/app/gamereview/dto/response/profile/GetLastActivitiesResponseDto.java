package com.app.gamereview.dto.response.profile;

import com.app.gamereview.enums.ActivityType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class GetLastActivitiesResponseDto {
    private String typeId;
    private ActivityType type;
    private String parentId;
    private String parentType;
    private String description;
    private LocalDateTime createdAt;



}
