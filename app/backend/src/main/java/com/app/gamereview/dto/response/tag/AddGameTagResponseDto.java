package com.app.gamereview.dto.response.tag;

import com.app.gamereview.model.Tag;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddGameTagResponseDto {
    private String gameId;

    private Tag addedTag;
}
