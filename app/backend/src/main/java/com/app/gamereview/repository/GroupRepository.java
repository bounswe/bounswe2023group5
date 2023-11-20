package com.app.gamereview.repository;

import com.app.gamereview.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GroupRepository extends MongoRepository<Group, String> {
    Optional<Group> findByIdAndIsDeletedFalse(String id);

    Optional<Group> findByTitleAndIsDeletedFalse(String title);
}
