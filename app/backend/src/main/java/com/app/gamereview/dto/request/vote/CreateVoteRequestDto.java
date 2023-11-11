package com.app.gamereview.dto.request.vote;


import com.app.gamereview.enums.VoteChoice;
import com.app.gamereview.enums.VoteType;
import com.app.gamereview.util.validation.annotation.ValidVoteChoice;
import com.app.gamereview.util.validation.annotation.ValidVoteType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVoteRequestDto {

    @ValidVoteType(allowedValues = {VoteType.POST, VoteType.GAME, VoteType.COMMENT, VoteType.REVIEW})
    private String voteType;

    @NotEmpty(message = "Type Id field cannot be null or empty")
    @Pattern(regexp = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$",
            message = "Type Id has Invalid Id (UUID) format")
    private String typeId;

    @ValidVoteChoice(allowedValues = {VoteChoice.UPVOTE, VoteChoice.DOWNVOTE})
    private String choice;
}
