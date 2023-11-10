package com.app.gamereview.dto.response.post;

import java.time.LocalDateTime;
import java.util.List;

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

  private String poster;

  // TODO avatar

  private LocalDateTime lastEditedAt;

  private LocalDateTime createdAt;

  private Boolean isEdited;

  private List<String> tags;

  private boolean inappropriate;

  // TODO reports
  // TODO comments
  // TODO annotations
  // TODO achievements
}
