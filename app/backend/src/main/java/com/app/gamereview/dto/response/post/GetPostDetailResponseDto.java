package com.app.gamereview.dto.response.post;

import com.app.gamereview.model.User;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private String forum;

    // TODO avatar

    private LocalDateTime lastEditedAt;

    private List<String> tags;

    private Boolean inappropriate;

    private Boolean locked;

    private int overallVote;

    private int voteCount;
}
