package com.app.gamereview.repository;

import com.app.gamereview.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;
import java.util.List;

public interface GroupRepository extends MongoRepository<Group, String> {
    Optional<Group> findByIdAndIsDeletedFalse(String id);

    Optional<Group> findByTitleAndIsDeletedFalse(String title);

    @Query("{ 'members' : ?0, 'bannedMembers' : { $nin: [?0] }, 'isDeleted' : false }")
    List<Group> findUserGroups(String userId);
}
