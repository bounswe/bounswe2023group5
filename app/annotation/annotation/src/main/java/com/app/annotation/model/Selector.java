package com.app.annotation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Selector {

    private String type;

    private String exact;

    private String prefix;

    private String suffix;

    public Map<String, String> toJSON() {
        Map<String, String> json = new HashMap<>();
        if  (this.type != null) json.put("type", this.type);
        if  (this.exact != null) json.put("exact", this.exact);
        if  (this.prefix != null) json.put("prefix", this.prefix);
        if  (this.suffix != null) json.put("suffix", this.suffix);
        return json;
    }
}
