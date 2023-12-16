package com.app.annotation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "Annotation")
@Getter
@Setter
@AllArgsConstructor
public class Annotation {

    @Id
    private String id;

    private String target;

    public Annotation(){
        this.id = UUID.randomUUID().toString(); // Generate a UUID during construction
    }
}
