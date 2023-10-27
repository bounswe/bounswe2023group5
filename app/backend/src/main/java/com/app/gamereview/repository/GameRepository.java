package com.app.gamereview.repository;

import com.app.gamereview.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GameRepository extends MongoRepository<Game, String> {

    Optional<Game> findByGameNameAndIsDeletedFalse(String gameName);

}
