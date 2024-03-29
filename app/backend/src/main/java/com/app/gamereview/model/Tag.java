package com.app.gamereview.model;

import com.app.gamereview.enums.TagType;
import com.app.gamereview.model.common.BaseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Tag")
@TypeAlias("Tag")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tag extends BaseModel {

    private String name;

    private TagType tagType;

    private String color;
}
