package com.app.gamereview.dto.request.post;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPostListFilterRequestDto {

  private Boolean findDeleted;

  private List<String> tags;

  private String search;

}
