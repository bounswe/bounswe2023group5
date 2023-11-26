package com.app.gamereview.repository;

import com.app.gamereview.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends MongoRepository<Profile, String> {

    Optional<Profile> findByUserIdAndIsDeletedFalse(String userId);

}
