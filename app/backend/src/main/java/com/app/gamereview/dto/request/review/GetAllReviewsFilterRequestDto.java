package com.app.gamereview.dto.request.review;

import com.app.gamereview.enums.SortDirection;
import com.app.gamereview.enums.SortType;
import com.app.gamereview.util.validation.annotation.ValidSortDirection;
import com.app.gamereview.util.validation.annotation.ValidSortType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllReviewsFilterRequestDto {
    private String gameId;

    private String reviewedBy;

    private Boolean withDeleted = false;

    @ValidSortType(allowedValues = {SortType.OVERALL_VOTE, SortType.CREATION_DATE, SortType.VOTE_COUNT})
    private String sortBy = SortType.CREATION_DATE.name();

    @ValidSortDirection(allowedValues = {SortDirection.ASCENDING, SortDirection.DESCENDING})
    private String sortDirection = SortDirection.DESCENDING.name();
}
