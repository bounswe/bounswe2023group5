package com.app.gamereview.model;

import com.app.gamereview.enums.VoteChoice;
import com.app.gamereview.model.common.BaseModel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Comment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseModel {

    private String commentContent;

    private String commenter;

    @NotNull
    private String post;

    private String parentComment;

    private LocalDateTime lastEditedAt;

    private int overallVote; // overallVote = # of upvote - # of downvote

    private int voteCount;  // voteCount = # of upvote + # of downvote

    // TODO reports
    // TODO annotations

    public void addVote(VoteChoice choice) {
        voteCount += 1;
        if (choice.name().equals("UPVOTE")) {
            overallVote += 1;
        } else if (choice.name().equals("DOWNVOTE")) {
            overallVote -= 1;
        }
    }

    public void deleteVote(VoteChoice choice) {
        voteCount -= 1;
        if (choice.name().equals("UPVOTE")) {
            overallVote -= 1;
        } else if (choice.name().equals("DOWNVOTE")) {
            overallVote += 1;
        }
    }


}
