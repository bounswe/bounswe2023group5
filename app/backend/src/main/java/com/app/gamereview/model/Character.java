package com.app.gamereview.model;

import com.app.gamereview.model.common.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Document(collection = "Character")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Character extends BaseModel {
    private String name;

    private String icon;

    private String description;

    private List<String> games;

    private String type;

    private String gender;

    private String race;

    private String status;

    private String occupation;

    private String birthDate;

    private String voiceActor;

    private String height;

    private String age;

    @Field("customFields")
    private Map<String, String> customFields;
}
