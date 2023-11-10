package com.app.gamereview.model;

import com.app.gamereview.model.common.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Review")
@TypeAlias("Review")
@Getter
@Setter
public class Review extends BaseModel {

    // default string value is null hence below assignment is not mandatory
    private String reviewDescription = null; // initialized as null since it is optional (increased readability)

    private float rating;

    private String gameId;

    private String reviewedBy;   // user Id

    private int overallVote; // overallVote = # of upvote - # of downvote

    private int voteCount;  // voteCount = # of upvote + # of downvote

    private int reportNum;
}
