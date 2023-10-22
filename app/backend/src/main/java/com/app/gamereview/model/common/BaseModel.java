package com.app.gamereview.model.common;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class BaseModel {

	@Id
	private String id;

	private LocalDateTime createdAt;

	private Boolean isDeleted;

	@PersistenceConstructor
	public BaseModel() {
		this.id = UUID.randomUUID().toString(); // Generate a UUID during construction
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Boolean getDeleted() {
		return isDeleted;
	}

	public void setDeleted(Boolean deleted) {
		isDeleted = deleted;
	}

}
