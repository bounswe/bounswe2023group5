package com.app.gamereview.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.gamereview.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends MongoRepository<Post, String> {
    Optional<Post> findByIdAndIsDeletedFalse(String postId);

    List<Post> findByForumAndIsDeletedFalse(String forum);

}
