package com.app.gamereview.repository;

import com.app.gamereview.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByUsernameAndIsDeletedFalse(String username);

	Optional<User> findByEmailAndIsDeletedFalse(String email);

	@Query("{ 'email' : ?0 }")
	Optional<User> findByEmail(String email);

}
