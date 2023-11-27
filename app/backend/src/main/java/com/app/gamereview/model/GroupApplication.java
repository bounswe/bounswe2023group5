package com.app.gamereview.model;

import com.app.gamereview.enums.GroupApplicationStatus;
import com.app.gamereview.model.common.BaseModel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "GroupApplication")
@TypeAlias("GroupApplication")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupApplication extends BaseModel {
    @NotNull
    private String user;

    @NotNull
    private String group;

    @NotNull
    private String message;

    @NotNull
    private GroupApplicationStatus status = GroupApplicationStatus.PENDING;

    private String reviewer;

    private LocalDateTime reviewedAt;
}
