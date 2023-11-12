package com.app.gamereview.dto.request.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditPostRequestDto {

  private String title;

  private String postContent;

}
