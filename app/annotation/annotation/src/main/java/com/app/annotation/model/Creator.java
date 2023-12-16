package com.app.annotation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Creator {

    private String id;

    private String type;

    private String name;

    private String nickname;

    public Map<String, String> toJSON() {
        Map<String, String> json = new HashMap<>();
        if  (this.type != null) json.put("type", this.type);
        if  (this.id != null) json.put("id", this.id);
        if  (this.name != null) json.put("name", this.name);
        if  (this.nickname != null) json.put("nickname", this.nickname);

        return json;
    }
}
