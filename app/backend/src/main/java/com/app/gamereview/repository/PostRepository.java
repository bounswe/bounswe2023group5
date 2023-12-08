package com.app.gamereview.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.gamereview.model.Post;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {

    List<Post> findByForumAndIsDeletedFalse(String forum);

}
