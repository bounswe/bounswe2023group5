package com.app.gamereview.dto.response.group;

import com.app.gamereview.enums.GroupApplicationStatus;
import com.app.gamereview.model.Group;
import com.app.gamereview.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GroupApplicationResponseDto {
    private String id;

    private LocalDateTime appliedAt;

    private User applicant;

    private Group group;

    private String message;

    private GroupApplicationStatus status;
}
