package com.app.gamereview.dto.request.tag;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddGameTagRequestDto {

    private String gameId;

    private String tagId;
}
