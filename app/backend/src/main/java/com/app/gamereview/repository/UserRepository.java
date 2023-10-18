package com.app.gamereview.repository;

import com.app.gamereview.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;
public interface UserRepository extends MongoRepository<User, UUID> {
}