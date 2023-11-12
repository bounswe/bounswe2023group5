package com.app.gamereview.repository;

import com.app.gamereview.model.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoteRepository extends MongoRepository<Vote, String> {
}
