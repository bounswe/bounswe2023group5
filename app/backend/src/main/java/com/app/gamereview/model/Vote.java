package com.app.gamereview.model;

import com.app.gamereview.enums.VoteChoice;
import com.app.gamereview.enums.VoteType;
import com.app.gamereview.model.common.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Vote")
@TypeAlias("Vote")
@Getter
@Setter
public class Vote extends BaseModel {

    private VoteType voteType;

    private String typeId;  // id of the voted thing (thing: review, post, etc.)

    private VoteChoice choice;

    private String votedBy; // user id
}
