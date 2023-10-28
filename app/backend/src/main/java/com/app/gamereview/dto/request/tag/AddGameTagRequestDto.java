package com.app.gamereview.dto.request.tag;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddGameTagRequestDto {

    private String gameId;

    private String tagId;
}
