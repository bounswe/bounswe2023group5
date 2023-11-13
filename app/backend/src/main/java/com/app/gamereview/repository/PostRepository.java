package com.app.gamereview.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.gamereview.model.Post;

public interface PostRepository extends MongoRepository<Post, String> {

}
