package com.app.gamereview.dto.response.post;

import java.time.LocalDateTime;
import java.util.List;

import com.app.gamereview.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPostListResponseDto {

  private String id;

  private String title;

  private String postContent;

  private User poster;

  // TODO avatar

  private LocalDateTime lastEditedAt;

  private LocalDateTime createdAt;

  private Boolean isEdited;

  private List<String> tags;

  private boolean inappropriate;

  private int overallVote; // overallVote = # of upvote - # of downvote

  private int voteCount;  // voteCount = # of upvote + # of downvote

  private int commentCount;

  // TODO reports
  // TODO comments
  // TODO annotations
  // TODO achievements
}
