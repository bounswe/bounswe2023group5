package com.app.annotation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private List<Selector> selector;

    public Map<String, Object> toJSON() {
        Map<String, Object> json = new HashMap<>();
        if  (this.id != null) json.put("id", this.id);
        if  (this.type != null) json.put("type", this.type);
        if  (this.format != null) json.put("format", this.format);
        if  (this.textDirection != null) json.put("textDirection", this.textDirection);
        if  (this.source != null) json.put("source", this.source);
        if  (this.selector != null && !this.selector.isEmpty()) {
            if (this.selector.size() > 1) {
                List<Map<String, Object>> selectorList = new ArrayList<>();

                for (Selector s : this.selector) {
                    selectorList.add(s.toJSON());
                }
                json.put("selector", selectorList);
            } else {
                json.put("selector", this.selector.get(0).toJSON());
            }
        }
        return json;
    }
}
