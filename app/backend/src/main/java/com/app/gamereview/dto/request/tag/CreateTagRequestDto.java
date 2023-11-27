package com.app.gamereview.dto.request.tag;

import com.app.gamereview.enums.TagType;

import com.app.gamereview.util.validation.annotation.ValidTagType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTagRequestDto {

    @NotEmpty(message = "Tag name cannot be null or empty")
    private String name;

    @ValidTagType(allowedValues = {TagType.ART_STYLE, TagType.GENRE, TagType.DURATION, TagType.OTHER,
            TagType.MONETIZATION, TagType.PLATFORM, TagType.DEVELOPER, TagType.PLAYER_TYPE, TagType.POST,
            TagType.PRODUCTION, TagType.GROUP})
    private String tagType;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Provided color must be in hexadecimal format, e.g. #FF4500")
    private String color;

}
