package com.app.gamereview.repository;

import com.app.gamereview.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByReviewedBy(String reviewerId);
}
