package com.app.annotation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "Annotation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Annotation {

    @Id
    private String id;

    private Target target;

    private Body body;

    private String type;

    private String motivation;

    private Date created;

    private Creator creator;

    public Map<String, Object> toJSON() {
        Map<String, Object> json = new HashMap<>();
        json.put("@context", "http://www.w3.org/ns/anno.jsonld");
        if  (this.id != null) json.put("id", this.id);
        if  (this.type != null) json.put("type", this.type);
        if  (this.motivation != null) json.put("motivation", this.motivation);
        if  (this.created != null) json.put("created", this.created);
        if  (this.target != null) json.put("target", this.target.toJSON());
        if  (this.body != null) json.put("body", this.body.toJSON());
        if  (this.creator != null) json.put("creator", this.creator.toJSON());
        return json;
    }
}
