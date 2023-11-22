package com.app.gamereview.dto.response.post;

import com.app.gamereview.model.Tag;
import com.app.gamereview.model.User;
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

    private String forum;

    // TODO avatar

    private String achievement;

    private LocalDateTime lastEditedAt;

    private List<Tag> tags;

    private Boolean inappropriate;

    private Boolean locked;

    private int overallVote;

    private int voteCount;
}
