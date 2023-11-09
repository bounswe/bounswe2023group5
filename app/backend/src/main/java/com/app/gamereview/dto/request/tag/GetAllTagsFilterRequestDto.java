package com.app.gamereview.dto.request.tag;

import com.app.gamereview.enums.TagType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllTagsFilterRequestDto {
    private String name;

    private String color;

    private String tagType;

    private Boolean isDeleted;
}
