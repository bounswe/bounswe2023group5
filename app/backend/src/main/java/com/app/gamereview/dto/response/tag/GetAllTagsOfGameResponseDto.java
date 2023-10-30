package com.app.gamereview.dto.response.tag;

import com.app.gamereview.model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetAllTagsOfGameResponseDto {
    private List<Tag> playerTypes;

    private List<Tag> genre;

    private Tag production;

    private Tag duration;

    private List<Tag> platforms;

    private List<Tag> artStyles;

    private Tag developer;

    private List<Tag> otherTags;
}
