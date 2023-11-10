package com.app.gamereview.dto.request.post;

import java.time.LocalDateTime;
import java.util.List;

import com.app.gamereview.model.Tag;
import com.app.gamereview.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequestDto {

  private String title;

  private String postContent;

  private User poster;

  // TODO avatar

  private List<Tag> tags;

  // TODO annotations
  // TODO achievements
}
