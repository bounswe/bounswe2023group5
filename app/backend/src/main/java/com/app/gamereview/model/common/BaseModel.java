package com.app.gamereview.model.common;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public abstract class BaseModel {

	@Id
	private String id;

	private LocalDateTime createdAt;

	private Boolean isDeleted;

	@PersistenceConstructor
	public BaseModel() {
		this.id = UUID.randomUUID().toString(); // Generate a UUID during construction
	}
}
