package com.app.gamereview.repository;

import com.app.gamereview.model.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TagRepository extends MongoRepository<Tag, String> {
    Optional<Tag> findByNameAndIsDeletedFalse(String name);

    Optional<Tag> findByIdAndIsDeletedFalse(String id);
}
