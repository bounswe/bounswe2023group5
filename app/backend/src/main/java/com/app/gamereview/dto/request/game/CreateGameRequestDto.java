package com.app.gamereview.dto.request.game;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGameRequestDto {
    @NotEmpty(message = "Game name cannot be null or empty")
    private String gameName;

    @NotEmpty(message = "Game description cannot be null or empty")
    private String gameDescription;

    @NotEmpty(message = "Game icon cannot be null or empty")
    private String gameIcon;

    @NotNull(message = "Release Date cannot be null or empty")
    private Date releaseDate;

    @NotEmpty(message = "Player types cannot be null or empty")
    private List<@Pattern(regexp = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$",
            message = "One of the player types has Invalid Id (UUID) format")String> playerTypes;

    @NotEmpty(message = "Genres cannot be null or empty")
    private List<@Pattern(regexp = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$",
            message = "One of the genres has Invalid Id (UUID) format")String> genre;

    @Pattern(regexp = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$",
            message = "Production has Invalid Id (UUID) format")
    private String production;

    @NotEmpty(message = "Game platforms cannot be null or empty")
    private List<@Pattern(regexp = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$",
            message = "One of the platforms has Invalid Id (UUID) format")String> platforms;


    private List<@Pattern(regexp = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$",
            message = "One of the artstyles has Invalid Id (UUID) format")String> artStyles;

    @NotEmpty(message = "Developer cannot be null or empty")
    @Pattern(regexp = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$",
            message = "Developer has Invalid Id (UUID) format")
    private String developer;

    private List<@Pattern(regexp = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$",
            message = "One of the other tags has Invalid Id (UUID) format")String> otherTags;

    @NotEmpty(message = "Minimum system requirements cannot be null or empty")
    private String minSystemReq;
}
