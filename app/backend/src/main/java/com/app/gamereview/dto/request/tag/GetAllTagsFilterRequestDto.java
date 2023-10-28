package com.app.gamereview.dto.request.tag;

import com.app.gamereview.enums.TagType;
import lombok.Getter;

@Getter
public class GetAllTagsFilterRequestDto {
    private String name;

    private String color;

    private TagType tagType;

    private Boolean isDeleted;
}
