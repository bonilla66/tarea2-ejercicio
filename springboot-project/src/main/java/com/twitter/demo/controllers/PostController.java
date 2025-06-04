package com.twitter.demo.controllers;

import com.twitter.demo.entities.Post;
import com.twitter.demo.entities.dto.*;
import com.twitter.demo.repositories.PostRepository;
import com.twitter.demo.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody CreatePostDto request){
        postService.createPost(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/likes")
    public ResponseEntity<?> likePost(@RequestBody @Valid LikeDto request){
        try {
            postService.likePost(request);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserPostsDto>> getAllPosts() {
        List<UserPostsDto> list = postService.getAllPosts();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/liked")
    public ResponseEntity<List<PostLikesDto>> getLikedPosts() {
        List<PostLikesDto> list = postService.getLikedPosts();
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/user/liked")
    public ResponseEntity<Void> removeLike(@RequestBody LikeDto request) {
        postService.removeLike(request.getUserId(), request.getPostId());
        return ResponseEntity.noContent().build();
    }

}
