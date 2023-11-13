package com.app.gamereview.controller;

import com.app.gamereview.dto.request.comment.CreateCommentRequestDto;
import com.app.gamereview.dto.request.comment.EditCommentRequestDto;
import com.app.gamereview.dto.request.comment.ReplyCommentRequestDto;
import com.app.gamereview.model.Comment;
import com.app.gamereview.model.User;
import com.app.gamereview.service.CommentService;
import com.app.gamereview.util.validation.annotation.AuthorizationRequired;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@Validated
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @AuthorizationRequired
    @PostMapping("/create")
    public ResponseEntity<Comment> createComment(@Valid @RequestBody CreateCommentRequestDto comment, @RequestHeader String Authorization, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        Comment commentToCreate = commentService.createComment(comment, user);
        return ResponseEntity.ok(commentToCreate);
    }

    @AuthorizationRequired
    @PostMapping("/reply")
    public ResponseEntity<Comment> createReply(@Valid @RequestBody ReplyCommentRequestDto reply, @RequestHeader String Authorization, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        Comment commentToCreate = commentService.replyComment(reply, user);
        return ResponseEntity.ok(commentToCreate);
    }

    @AuthorizationRequired
    @PostMapping("/edit")
    public ResponseEntity<Comment> editComment(@RequestParam String id, @Valid @RequestBody EditCommentRequestDto comment, @RequestHeader String Authorization, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        Comment editedComment = commentService.editComment(id, comment, user);
        return ResponseEntity.ok(editedComment);
    }

    @AuthorizationRequired
    @DeleteMapping("/delete")
    public ResponseEntity<Comment> deleteComment(@RequestParam String id, @RequestHeader String Authorization, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        Comment deletedComment = commentService.deleteComment(id, user);
        return ResponseEntity.ok(deletedComment);
    }
}
