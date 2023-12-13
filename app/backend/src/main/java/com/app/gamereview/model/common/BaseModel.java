package com.app.gamereview.model.common;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class BaseModel {

	@Id
	private String id;

	private LocalDateTime createdAt;

	private Boolean isDeleted;

	@PersistenceCreator
	public BaseModel() {
		this.id = UUID.randomUUID().toString(); // Generate a UUID during construction
		this.createdAt = LocalDateTime.now();
		this.isDeleted = false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BaseModel given = (BaseModel) o;
		return id.equals(given.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
