package com.app.gamereview.dto.request.game;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateGameRequestDto {
    @NotEmpty(message = "Game name cannot be null or empty")
    private String gameName;

    @NotEmpty(message = "Game description cannot be null or empty")
    private String gameDescription;

    @NotEmpty(message = "Game icon cannot be null or empty")
    private String gameIcon;

    @NotEmpty(message = "Minimum system requirements cannot be null or empty")
    private String minSystemReq;
}
