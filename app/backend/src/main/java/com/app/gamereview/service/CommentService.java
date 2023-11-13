package com.app.gamereview.service;

import com.app.gamereview.dto.request.comment.CreateCommentRequestDto;
import com.app.gamereview.dto.request.comment.EditCommentRequestDto;
import com.app.gamereview.dto.request.comment.ReplyCommentRequestDto;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.*;
import com.app.gamereview.repository.CommentRepository;
import com.app.gamereview.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentService(PostRepository postRepository, CommentRepository commentRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }


    public Comment createComment(CreateCommentRequestDto request, User user) {

        Optional<Post> post = postRepository.findById(request.getPost());

        if (post.isEmpty()) {
            throw new ResourceNotFoundException("Post is not found.");
        }

        Comment commentToCreate = modelMapper.map(request, Comment.class);
        commentToCreate.setCommenter(user.getId());
        commentToCreate.setLastEditedAt(commentToCreate.getCreatedAt());
        return commentRepository.save(commentToCreate);
    }

    public Comment replyComment(ReplyCommentRequestDto request, User user) {

        Optional<Comment> parentComment = commentRepository.findById(request.getParentComment());

        if (parentComment.isEmpty()) {
            throw new ResourceNotFoundException("Parent Comment is not found.");
        }

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

        if (!comment.get().getCommenter().equals(user.getId())) {
            throw new BadRequestException("Only the user that created the comment can edit it.");
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

        if (!comment.get().getCommenter().equals(user.getId())) {
            throw new BadRequestException("Only the user that created the comment can delete it.");
        }

        Comment commentToDelete = comment.get();

        commentToDelete.setIsDeleted(true);

        return commentRepository.save(commentToDelete);
    }
}
