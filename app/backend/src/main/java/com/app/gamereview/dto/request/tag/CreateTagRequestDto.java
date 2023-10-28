package com.app.gamereview.dto.request.tag;

import com.app.gamereview.enums.TagType;
import lombok.Getter;

@Getter
public class CreateTagRequestDto {

    private String name;

    private TagType tagType;

    private String color;

}
