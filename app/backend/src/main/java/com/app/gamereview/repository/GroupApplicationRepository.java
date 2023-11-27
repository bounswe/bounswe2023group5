package com.app.gamereview.repository;

import com.app.gamereview.enums.GroupApplicationStatus;
import com.app.gamereview.model.GroupApplication;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface GroupApplicationRepository extends MongoRepository<GroupApplication, String> {
    Optional<GroupApplication> findByUserAndGroupAndStatus(String userId, String groupId, GroupApplicationStatus status);

    List<GroupApplication> findByGroupAndStatus(String groupId, GroupApplicationStatus groupApplicationStatus);
}
