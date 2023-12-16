package com.app.annotation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Target {
    private String id;

    private String type;

    private String format;

    private String textDirection;

    private String source;

    private Selector selector;

    public Map<String, Object> toJSON() {
        Map<String, Object> json = new HashMap<>();
        if  (this.id != null) json.put("id", this.id);
        if  (this.type != null) json.put("type", this.type);
        if  (this.format != null) json.put("format", this.format);
        if  (this.textDirection != null) json.put("textDirection", this.textDirection);
        if  (this.source != null) json.put("source", this.source);
        if  (this.selector != null) json.put("selector", this.selector.toJSON());
        return json;
    }
}
