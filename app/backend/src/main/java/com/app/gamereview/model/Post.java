package com.app.gamereview.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.app.gamereview.model.common.BaseModel;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "Post")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseModel {

  private String title;

  private String postContent;

  private User poster;

  @NotNull
  private String forum;

  // TODO avatar

  private LocalDateTime lastEditedAt;

  private List<Tag> tags;

  private Boolean inappropriate;

  private Boolean locked;

  // TODO reports
  // TODO comments
  // TODO annotations
  // TODO achievements

}
