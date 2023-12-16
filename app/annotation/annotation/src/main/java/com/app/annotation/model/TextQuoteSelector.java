package com.app.annotation.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class TextQuoteSelector extends Selector {
    private String exact;

    private String prefix;

    private String suffix;

    @Override
    public Map<String, String> toJSON() {
        Map<String, String> json = new HashMap<>();
        //json.put("type", this.type);
        json.put("exact", this.exact);
        json.put("prefix", this.prefix);
        json.put("suffix", this.suffix);
        return json;
    }
}
