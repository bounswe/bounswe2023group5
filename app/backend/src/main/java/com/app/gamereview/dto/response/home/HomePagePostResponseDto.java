package com.app.gamereview.dto.response.home;

import com.app.gamereview.enums.ForumType;
import com.app.gamereview.enums.VoteChoice;
import com.app.gamereview.model.Achievement;
import com.app.gamereview.model.Character;
import com.app.gamereview.model.Tag;
import com.app.gamereview.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HomePagePostResponseDto {

    private String id;

    private String title;

    private String postContent;

    private String postImage;

    private User poster;

    private Achievement achievement;

    private Character character;

    private VoteChoice userVote;

    @NotNull
    private String forum;

    private ForumType type;

    private String typeId;

    private String typeName;

    // TODO avatar

    private LocalDateTime lastEditedAt;

    private List<Tag> tags = new ArrayList<>();

    private Boolean inappropriate;

    private Boolean locked;

    private int overallVote; // overallVote = # of upvote - # of downvote

    private int voteCount;  // voteCount = # of upvote + # of downvote

    private LocalDateTime createdAt;

    private Boolean isDeleted;
    private Boolean isPromoted;

}
