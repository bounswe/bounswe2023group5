package com.app.gamereview.repository;

import com.app.gamereview.model.Character;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CharacterRepository extends MongoRepository<Character, String> {

    Optional<Character> findByIdAndIsDeletedFalse(String id);

    List<Character> findByGamesContains(String game);

}
