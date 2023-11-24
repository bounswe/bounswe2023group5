package com.app.gamereview.service;

import com.app.gamereview.dto.request.comment.CreateCommentRequestDto;
import com.app.gamereview.dto.request.comment.EditCommentRequestDto;
import com.app.gamereview.dto.request.comment.ReplyCommentRequestDto;
import com.app.gamereview.enums.UserRole;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.*;
import com.app.gamereview.repository.AchievementRepository;
import com.app.gamereview.repository.CommentRepository;
import com.app.gamereview.repository.PostRepository;
import com.app.gamereview.repository.ProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ProfileRepository profileRepository;
    private final AchievementRepository achievementRepository;
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CommentService(PostRepository postRepository, CommentRepository commentRepository,
                          ProfileRepository profileRepository, AchievementRepository achievementRepository,
                          MongoTemplate mongoTemplate, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.profileRepository = profileRepository;
        this.achievementRepository = achievementRepository;
        this.modelMapper = modelMapper;
        this.mongoTemplate = mongoTemplate;
    }


    public Comment createComment(CreateCommentRequestDto request, User user) {

        Optional<Post> post = postRepository.findById(request.getPost());

        if (post.isEmpty()) {
            throw new ResourceNotFoundException("Post is not found.");
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(user.getId()));
        Profile profile = mongoTemplate.findOne(query, Profile.class);

        if(profile == null){
            throw new ResourceNotFoundException("Profile of the user does not exist");
        }

        if(!profile.getIsCommentedYet()){      // first review of the user
            Optional<Achievement> achievement =
                    achievementRepository.findByIdAndIsDeletedFalse("af009796-6799-42d4-ae40-9adbb92657c4");
            achievement.ifPresent(value -> profile.addAchievement(value.getId()));
            profile.setIsCommentedYet(true);
        }

        profile.setCommentCount(profile.getCommentCount() + 1);
        profileRepository.save(profile);

        Comment commentToCreate = modelMapper.map(request, Comment.class);
        commentToCreate.setCommenter(user.getId());
        commentToCreate.setLastEditedAt(commentToCreate.getCreatedAt());
        return commentRepository.save(commentToCreate);
    }

    public Comment replyComment(ReplyCommentRequestDto request, User user) {

        Optional<Comment> parentComment = commentRepository.findById(request.getParentComment());

        if (parentComment.isEmpty() || parentComment.get().getIsDeleted()) {
            throw new ResourceNotFoundException("Parent Comment is not found.");
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(user.getId()));
        Profile profile = mongoTemplate.findOne(query, Profile.class);

        if(profile == null){
            throw new ResourceNotFoundException("Profile of the user does not exist");
        }

        if(!profile.getIsCommentedYet()){      // first review of the user
            Optional<Achievement> achievement =
                    achievementRepository.findByIdAndIsDeletedFalse("af009796-6799-42d4-ae40-9adbb92657c4");
            achievement.ifPresent(value -> profile.addAchievement(value.getId()));
            profile.setIsCommentedYet(true);
        }

        profile.setCommentCount(profile.getCommentCount() + 1);
        profileRepository.save(profile);

        Comment commentToCreate = modelMapper.map(request, Comment.class);
        commentToCreate.setCommenter(user.getId());
        commentToCreate.setLastEditedAt(commentToCreate.getCreatedAt());
        commentToCreate.setPost(parentComment.get().getPost());
        return commentRepository.save(commentToCreate);
    }

    public Comment editComment(String id, EditCommentRequestDto request, User user) {
        Optional<Comment> comment = commentRepository.findById(id);

        if (comment.isEmpty() || comment.get().getIsDeleted()) {
            throw new ResourceNotFoundException("The comment with the given id is not found.");
        }

        if (!(comment.get().getCommenter().equals(user.getId()) || UserRole.ADMIN.equals(user.getRole()))) {
            throw new BadRequestException("Only the user that created the comment or admin can edit it.");
        }

        Comment editedComment = comment.get();

        if (request.getCommentContent() != null && !request.getCommentContent().isEmpty()) {
            editedComment.setCommentContent(request.getCommentContent());
            editedComment.setLastEditedAt(LocalDateTime.now());
        }

        return commentRepository.save(editedComment);
    }

    public Comment deleteComment(String id, User user) {
        Optional<Comment> comment = commentRepository.findById(id);

        if (comment.isEmpty() || comment.get().getIsDeleted()) {
            throw new ResourceNotFoundException("The comment with the given id is not found.");
        }

        if (!(comment.get().getCommenter().equals(user.getId()) || UserRole.ADMIN.equals(user.getRole()))) {
            throw new BadRequestException("Only the user that created the comment or admin can delete it.");
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(user.getId()));
        Profile profile = mongoTemplate.findOne(query, Profile.class);

        if(profile == null){
            throw new ResourceNotFoundException("Profile of the user does not exist");
        }

        profile.setCommentCount(profile.getCommentCount() - 1);
        profileRepository.save(profile);

        Comment commentToDelete = comment.get();

        commentToDelete.setIsDeleted(true);

        return commentRepository.save(commentToDelete);
    }
}
