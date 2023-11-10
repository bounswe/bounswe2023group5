package com.app.gamereview.dto.response.tag;

import com.app.gamereview.model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetAllTagsOfGameResponseDto {
    private List<String> playerTypes;

    private List<String> genre;

    private String production;

    private String duration;

    private List<String> platforms;

    private List<String> artStyles;

    private String developer;

    private List<String> otherTags;
}
