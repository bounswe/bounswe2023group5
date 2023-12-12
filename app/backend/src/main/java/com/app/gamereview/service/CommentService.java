package com.app.gamereview.service;

import com.app.gamereview.dto.request.comment.CreateCommentRequestDto;
import com.app.gamereview.dto.request.comment.EditCommentRequestDto;
import com.app.gamereview.dto.request.comment.ReplyCommentRequestDto;
import com.app.gamereview.dto.request.notification.CreateNotificationRequestDto;
import com.app.gamereview.dto.request.post.GetPostListFilterRequestDto;
import com.app.gamereview.dto.response.post.GetPostListResponseDto;
import com.app.gamereview.enums.*;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.*;
import com.app.gamereview.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ProfileRepository profileRepository;
    private final AchievementRepository achievementRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;
    private final NotificationService notificationService;


    @Autowired
    public CommentService(PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository,
                          ProfileRepository profileRepository, AchievementRepository achievementRepository,
                          MongoTemplate mongoTemplate, ModelMapper modelMapper, NotificationService notificationService) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.profileRepository = profileRepository;
        this.achievementRepository = achievementRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.mongoTemplate = mongoTemplate;
        this.notificationService = notificationService;

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
            CreateNotificationRequestDto createNotificationRequestDto= new CreateNotificationRequestDto();
            String message = NotificationMessage.FIRST_COMMENT_ACHIEVEMENT.getMessageTemplate()
                    .replace("{user_name}", user.getUsername())
                    .replace("{post_title}", post.get().getTitle());
            createNotificationRequestDto.setMessage(message);
            createNotificationRequestDto.setParentType(NotificationParent.ACHIEVEMENT);
            createNotificationRequestDto.setUser(user.getId());
            notificationService.createNotification(createNotificationRequestDto);
        }

        profile.setCommentCount(profile.getCommentCount() + 1);
        profileRepository.save(profile);

        Comment commentToCreate = modelMapper.map(request, Comment.class);
        commentToCreate.setCommenter(user.getId());
        commentToCreate.setLastEditedAt(commentToCreate.getCreatedAt());

        Comment savedComment = commentRepository.save(commentToCreate);

        if(!user.getId().equals(post.get().getPoster())){
            CreateNotificationRequestDto createNotificationRequestDto= new CreateNotificationRequestDto();
            Optional<User> postOwner = userRepository.findByIdAndIsDeletedFalse(post.get().getPoster());
            if (postOwner.isPresent()) {
                String message = NotificationMessage.NEW_COMMENT_FOR_THE_POST.getMessageTemplate()
                        .replace("{user_name}", postOwner.get().getUsername())
                        .replace("{post_title}", post.get().getTitle());
                createNotificationRequestDto.setMessage(message);
                createNotificationRequestDto.setParentType(NotificationParent.POST);
                createNotificationRequestDto.setParent(post.get().getId());
                createNotificationRequestDto.setUser(post.get().getPoster());
                notificationService.createNotification(createNotificationRequestDto);
            }
        }

        return savedComment;
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

        Comment savedComment = commentRepository.save(commentToCreate);

        if(!user.getId().equals(parentComment.get().getCommenter())){
            CreateNotificationRequestDto createNotificationRequestDto= new CreateNotificationRequestDto();
            Optional<Post> post = postRepository.findById(parentComment.get().getPost());
            if (post.isPresent()) {
                Optional<User> commentOwner = userRepository.findByIdAndIsDeletedFalse(parentComment.get().getCommenter());
                if (commentOwner.isPresent()) {
                    String message = NotificationMessage.NEW_REPLY_FOR_THE_COMMENT.getMessageTemplate()
                            .replace("{user_name}", commentOwner.get().getUsername())
                            .replace("{post_title}", post.get().getTitle());
                    createNotificationRequestDto.setMessage(message);
                    createNotificationRequestDto.setParentType(NotificationParent.COMMENT);
                    createNotificationRequestDto.setParent(post.get().getId());
                    createNotificationRequestDto.setUser(parentComment.get().getCommenter());
                    notificationService.createNotification(createNotificationRequestDto);
                }
            }
        }
        return savedComment;
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

    public List<Comment> getUserCommentList(User user) {

        Query query = new Query();

        query.addCriteria(Criteria.where("commenter").is(user.getId()));

        Sort.Direction sortDirection = Sort.Direction.DESC; // Default sorting direction (you can change it to ASC if needed)

        query.with(Sort.by(sortDirection, "createdAt"));


        return mongoTemplate.find(query, Comment.class);
    }
}
