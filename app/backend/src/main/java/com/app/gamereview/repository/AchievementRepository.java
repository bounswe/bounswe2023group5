package com.app.gamereview.repository;

import com.app.gamereview.model.Achievement;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AchievementRepository extends MongoRepository<Achievement, String> {

    Optional<Achievement> findByTitleAndIsDeletedFalse(String title);

    Optional<Achievement> findByIdAndIsDeletedFalse(String id);
}
