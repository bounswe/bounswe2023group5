package com.app.gamereview.repository;

import com.app.gamereview.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    Optional<Notification> findByUserIdAndIsDeletedFalse(String userId);

}