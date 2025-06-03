package com.twitter.demo.controllers;

import com.twitter.demo.entities.Post;
import com.twitter.demo.entities.dto.CreatePostDto;
import com.twitter.demo.entities.dto.LikeDto;
import com.twitter.demo.entities.dto.UserPostsDto;
import com.twitter.demo.repositories.PostRepository;
import com.twitter.demo.services.PostService;
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
    public ResponseEntity<Void> likePost(@RequestBody LikeDto request){
        postService.likePost(request.getUserId(), request.getPostId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserPostsDto>> getAllPosts() {
        List<UserPostsDto> list = postService.getAllPosts();
        return ResponseEntity.ok(list);
    }



}
