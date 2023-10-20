package com.app.gamereview.repository;

import com.app.gamereview.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByIsDeletedFalse();
}
