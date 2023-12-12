package com.app.gamereview.dto.response.post;

import com.app.gamereview.model.Achievement;
import com.app.gamereview.enums.VoteChoice;
import com.app.gamereview.model.Tag;
import com.app.gamereview.model.User;
import com.app.gamereview.model.Character;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPostDetailResponseDto {

    private String title;

    private String postContent;

    private String postImage;

    private User poster;

    private VoteChoice userVote;

    private String forum;

    // TODO avatar

    private Achievement achievement;

    private Character character;

    private LocalDateTime lastEditedAt;

    private List<Tag> tags;

    private Boolean inappropriate;

    private Boolean locked;

    private int overallVote;

    private int voteCount;
}
