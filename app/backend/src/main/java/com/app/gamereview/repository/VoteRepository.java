package com.app.gamereview.repository;

import com.app.gamereview.model.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VoteRepository extends MongoRepository<Vote, String> {

    Optional<Vote> findByTypeIdAndVotedBy(String typeId, String votedBy);
}
