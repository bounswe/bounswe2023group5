package com.app.gamereview.dto.request.character;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCharacterRequestDto {

    private String name;

    private String icon;

    private String description;

    private List<@Pattern(regexp = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$",
            message = "Game has invalid Id (UUID) format")String> games;

    private String type;

    private String gender;

    private String race;

    private String status;

    private String occupation;

    private String birthDate;

    private String voiceActor;

    private String height;

    private String age;

    private Map<String, String> customFields;
}
