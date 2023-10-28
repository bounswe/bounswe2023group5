package com.app.gamereview.repository;

import com.app.gamereview.model.Tag;
import com.app.gamereview.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface TagRepository extends MongoRepository<Tag, String> {
    Optional<Tag> findByNameAndIsDeletedFalse(String name);
}
