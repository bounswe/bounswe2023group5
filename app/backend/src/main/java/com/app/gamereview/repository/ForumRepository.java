package com.app.gamereview.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.gamereview.model.Forum;

import java.util.Optional;

public interface ForumRepository extends MongoRepository<Forum, String> {

    Optional<Forum> findByIdAndIsDeletedFalse(String id);
}
