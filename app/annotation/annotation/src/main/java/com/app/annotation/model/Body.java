package com.app.annotation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Body {
    private String type;

    private String id;

    private String purpose;

    private String value;

    private String format;

    private String language;

    public Map<String, String> toJSON() {
        Map<String, String> json = new HashMap<>();
        if  (this.type != null) json.put("type", this.type);
        if  (this.id != null) json.put("id", this.id);
        if  (this.purpose != null) json.put("purpose", this.purpose);
        if  (this.value != null) json.put("value", this.value);
        if  (this.format != null) json.put("format", this.format);
        if  (this.language != null) json.put("language", this.language);

        return json;
    }
}
