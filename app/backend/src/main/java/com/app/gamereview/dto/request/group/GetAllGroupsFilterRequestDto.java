package com.app.gamereview.dto.request.group;

import com.app.gamereview.enums.SortDirection;
import com.app.gamereview.enums.SortType;
import com.app.gamereview.util.validation.annotation.ValidSortDirection;
import com.app.gamereview.util.validation.annotation.ValidSortType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GetAllGroupsFilterRequestDto {
    private String title;

    private String membershipPolicy;

    private List<String> tags = new ArrayList<>(); // list of tag ids

    private String gameName;   // id of related game

    @ValidSortType(allowedValues = {SortType.QUOTA, SortType.CREATION_DATE})
    private String sortBy = SortType.CREATION_DATE.name();

    @ValidSortDirection(allowedValues = {SortDirection.ASCENDING, SortDirection.DESCENDING})
    private String sortDirection = SortDirection.DESCENDING.name();
}
