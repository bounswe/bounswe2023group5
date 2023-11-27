package com.app.gamereview.dto.request.profile;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileRequestDto {

    @Size(min = 3, message = "Username must be at least 3 characters long")
    @Size(max = 15, message = "Username must be at most 15 characters long")
    private String username;

    private Boolean isPrivate;

    private String profilePhoto;

    private String steamProfile;

    private String epicGamesProfile;

    private String xboxProfile;

}
