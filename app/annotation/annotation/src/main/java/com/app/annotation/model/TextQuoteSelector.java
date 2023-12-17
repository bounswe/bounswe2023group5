package com.app.annotation.model;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class TextQuoteSelector extends Selector {

    private String exact;

    private String prefix;

    private String suffix;

    @Override
    public Map<String, Object> toJSON() {
        Map<String, Object> json = new HashMap<>();
        if  (this.type != null) json.put("type", this.type);
        if  (this.exact != null) json.put("exact", this.exact);
        if  (this.prefix != null) json.put("prefix", this.prefix);
        if  (this.suffix != null) json.put("suffix", this.suffix);
        return json;
    }
}
