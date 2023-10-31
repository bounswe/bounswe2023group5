package com.app.gamereview.dto.request.tag;

import com.app.gamereview.enums.TagType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateTagRequestDto {

    private String name;

    private TagType tagType;

    private String color;

}
