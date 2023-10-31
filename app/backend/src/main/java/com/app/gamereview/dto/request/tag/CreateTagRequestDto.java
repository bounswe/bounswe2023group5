package com.app.gamereview.dto.request.tag;

import com.app.gamereview.enums.TagType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTagRequestDto {

    private String name;

    private TagType tagType;

    private String color;

}
