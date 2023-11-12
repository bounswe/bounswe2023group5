package com.app.gamereview.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.gamereview.model.Forum;

public interface ForumRepository extends MongoRepository<Forum, String> {

}
