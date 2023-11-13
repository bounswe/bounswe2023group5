package com.app.gamereview.dto.request.post;

import java.util.List;

import com.app.gamereview.enums.SortDirection;
import com.app.gamereview.enums.SortType;
import com.app.gamereview.util.validation.annotation.ValidSortDirection;
import com.app.gamereview.util.validation.annotation.ValidSortType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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

  @NotEmpty(message = "Forum can not be empty or null.")
  @Pattern(regexp = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$",
          message = "Forum has invalid Id (UUID) format")
  private String forum;

  @ValidSortType(allowedValues = {SortType.CREATION_DATE, SortType.EDIT_DATE, SortType.OVERALL_VOTE, SortType.CREATION_DATE})
  private String sortBy;

  @ValidSortDirection(allowedValues = {SortDirection.ASCENDING, SortDirection.DESCENDING})
  private String sortDirection;

}
