package com.app.annotation.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
public abstract class Selector {

    String type;

    public abstract Map<String, Object> toJSON();

}
