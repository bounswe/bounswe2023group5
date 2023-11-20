package com.app.gamereview.dto.response.tag;

import com.app.gamereview.model.Tag;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddGroupTagResponseDto {
    private String groupId;

    private Tag addedTag;
}
