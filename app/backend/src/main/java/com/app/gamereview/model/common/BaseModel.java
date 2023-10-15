package com.app.gamereview.model.common;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class BaseModel {
    @Id
    public String id;

    public LocalDateTime createdAt;

    public Boolean isDeleted;
    @PersistenceConstructor
    public BaseModel() {
        this.id = UUID.randomUUID().toString(); // Generate a UUID during construction
    }
}
