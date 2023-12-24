package com.app.annotation.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class FragmentSelector extends Selector {

    private String value;

    private String conformsTo;

    @Override
    public Map<String, Object> toJSON() {
        Map<String, Object> json = new HashMap<>();
        if  (this.type != null) json.put("type", this.type);
        if  (this.value != null) json.put("value", this.value);
        if  (this.conformsTo != null) json.put("conformsTo", this.conformsTo);
        return json;
    }
}
