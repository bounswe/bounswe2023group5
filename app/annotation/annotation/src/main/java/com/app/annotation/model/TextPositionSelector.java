package com.app.annotation.model;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class TextPositionSelector extends Selector {
    private Integer start;

    private Integer end;

    @Override
    public Map<String, Object> toJSON() {
        Map<String, Object> json = new HashMap<>();
        if  (this.type != null) json.put("type", this.type);
        if  (this.start != null) json.put("start", this.start);
        if  (this.end != null) json.put("end", this.end);
        return json;
    }
}
