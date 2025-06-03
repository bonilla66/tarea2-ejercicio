package com.twitter.demo.controllers;

import com.twitter.demo.entities.Comments;
import com.twitter.demo.entities.dto.CommentDto;
import com.twitter.demo.entities.dto.CreateCommentDto;
import com.twitter.demo.entities.dto.UpdateCommentDto;
import com.twitter.demo.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    private static final Pattern UUID_REGEX = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
    );

    @PostMapping("/create")
    public ResponseEntity<CommentDto> createComment(@RequestBody @Valid CreateCommentDto dto) {
        CommentDto created = commentService.createComment(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto>> getCommentsByPost(@PathVariable String postId) {
        if (postId == null || !UUID_REGEX.matcher(postId).matches()) {
            return ResponseEntity.badRequest().build();
        }
        UUID pid = UUID.fromString(postId);
        List<CommentDto> list = commentService.getCommentsByPost(pid);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentDto>> getCommentsByUser(@PathVariable String userId) {
        if (userId == null || !UUID_REGEX.matcher(userId).matches()) {
            return ResponseEntity.badRequest().build();
        }
        UUID uid = UUID.fromString(userId);
        List<CommentDto> list = commentService.getCommentsByUser(uid);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommentDto>> getAllComments() {
        List<Comments> comments = commentService.getAllComments();
        List<CommentDto> dtos = new ArrayList<>();
        comments.forEach(com -> dtos.add(new CommentDto(
                com.getId(),
                com.getAuthor().getId(),
                com.getAuthor().getName(),
                com.getPost().getId(),
                com.getMessage()
        )));
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable String commentId) {
        if (commentId == null || !UUID_REGEX.matcher(commentId).matches()) {
            return ResponseEntity.badRequest().build();
        }
        UUID cid = UUID.fromString(commentId);
        CommentDto dto = commentService.getCommentById(cid);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable String commentId, @RequestBody @Valid UpdateCommentDto dto) {
        if (commentId == null || !UUID_REGEX.matcher(commentId).matches()) {
            return ResponseEntity.badRequest().build();
        }
        UUID cid = UUID.fromString(commentId);
        CommentDto updated = commentService.updateComment(cid, dto.getMessage());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId) {
        if (commentId == null || !UUID_REGEX.matcher(commentId).matches()) {
            return ResponseEntity.badRequest().build();
        }
        UUID cid = UUID.fromString(commentId);
        commentService.deleteComment(cid);
        return ResponseEntity.noContent().build();
    }

}
