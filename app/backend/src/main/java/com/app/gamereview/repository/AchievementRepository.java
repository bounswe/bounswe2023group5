package com.app.gamereview.repository;

import com.app.gamereview.model.Achievement;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AchievementRepository extends MongoRepository<Achievement, String> {

    List<Achievement> findByTitleAndIsDeletedFalse(String title);

    Optional<Achievement> findByIdAndIsDeletedFalse(String id);

    List<Achievement> findByGameAndIsDeletedFalse(String game);

    int countByGame(String game);
}
