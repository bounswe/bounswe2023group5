package com.app.gamereview.dto.request.vote;

import com.app.gamereview.enums.VoteChoice;
import com.app.gamereview.enums.VoteType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllVotesFilterRequestDto {

    private String voteType;

    private String typeId;

    private String choice;

    private String votedBy;

    private Boolean withDeleted = false;
}
