package com.app.gamereview.dto.response.comment;

import com.app.gamereview.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReplyResponseDto {

    private String id;

    private String commentContent;

    private User commenter;

    private String post;

    private LocalDateTime lastEditedAt;

    private LocalDateTime createdAt;

    private Boolean isEdited;

    private int overallVote; // overallVote = # of upvote - # of downvote

    private int voteCount;  // voteCount = # of upvote + # of downvote

    // TODO reports
    // TODO annotations
}
