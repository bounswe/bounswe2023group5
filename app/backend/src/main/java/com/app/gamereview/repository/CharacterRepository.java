package com.app.gamereview.repository;

import com.app.gamereview.model.Character;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CharacterRepository extends MongoRepository<Character, String> {
}
