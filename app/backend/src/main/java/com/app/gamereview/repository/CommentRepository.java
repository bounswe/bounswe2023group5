package com.app.gamereview.repository;

import com.app.gamereview.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPost(String postId);

    int countByPost(String postId);
}
