package com.app.gamereview.dto.request.post;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequestDto {

  @NotBlank(message = "Post title cannot be empty.")
  @NotNull(message = "Post title cannot be null.")
  private String title;

  private String postContent;

  private String postImage;

  @NotNull(message = "Forum Id cannot be null or empty.")
  @Pattern(regexp = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$",
          message = "Forum has invalid Id (UUID) format")
  private String forum;

  // TODO avatar

  private List<@Pattern(regexp = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$",
          message = "One of the tags has invalid Id (UUID) format")String> tags;

  // TODO annotations
  // TODO achievements
}
