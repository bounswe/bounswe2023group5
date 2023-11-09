package com.app.gamereview.dto.request.tag;

import com.app.gamereview.enums.TagType;
import com.app.gamereview.util.validation.annotation.ValidTagType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTagRequestDto {

    @NotEmpty(message = "Tag name cannot be null or empty")
    private String name;

    @ValidTagType(message = "Provided tag type is invalid")
    private TagType tagType;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Provided color must be in hexadecimal format, e.g. #FF4500")
    private String color;
}
