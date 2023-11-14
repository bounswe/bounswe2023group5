package com.app.gamereview.dto.request.comment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCommentRequestDto {

    private String commentContent;


    @NotNull(message = "Parent Comment Id cannot be null or empty.")
    @Pattern(regexp = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$",
            message = "Comment has invalid Id (UUID) format")
    private String parentComment;

    // TODO annotations
    // TODO achievements
}
