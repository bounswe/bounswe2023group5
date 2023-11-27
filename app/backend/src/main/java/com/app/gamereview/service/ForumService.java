package com.app.gamereview.service;

import com.app.gamereview.enums.UserRole;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.Forum;
import com.app.gamereview.model.Post;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.ForumRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForumService {


    private final ForumRepository forumRepository;

    private final MongoTemplate mongoTemplate;
    private final ModelMapper modelMapper;

    @Autowired
    public ForumService(ForumRepository forumRepository, MongoTemplate mongoTemplate, ModelMapper modelMapper) {
        this.forumRepository = forumRepository;
        this.mongoTemplate = mongoTemplate;
        this.modelMapper = modelMapper;
    }
    public Boolean banUser(String forumId, String userId) {
        Optional<Forum> forum = forumRepository.findById(forumId);

        if (forum.isEmpty() || forum.get().getIsDeleted()) {
            throw new ResourceNotFoundException("The forum with the given id is not found.");
        }

        forum.get().addBannedUser(userId);
        forumRepository.save(forum.get());

        return true;
    }

    public Boolean unbanUser(String forumId, String userId) {
        Optional<Forum> forum = forumRepository.findById(forumId);

        if (forum.isEmpty() || forum.get().getIsDeleted()) {
            throw new ResourceNotFoundException("The forum with the given id is not found.");
        }

        forum.get().removeBannedUser(userId);
        forumRepository.save(forum.get());

        return true;
    }

}