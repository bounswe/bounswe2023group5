package com.app.gamereview.dto.request.tag;

import com.app.gamereview.enums.TagType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTagRequestDto {

    private String name;

    private TagType tagType;

    private String color;
}
