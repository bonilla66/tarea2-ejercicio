package com.twitter.demo.controllers;

import com.twitter.demo.entities.Post;
import com.twitter.demo.entities.User;
import com.twitter.demo.entities.dto.RegisterDto;
import com.twitter.demo.entities.dto.UserDto;
import com.twitter.demo.entities.dto.UserPostsDto;
import com.twitter.demo.services.PostService;
import com.twitter.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    private Pattern UUID_REGEX =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody @Valid RegisterDto info){
        try {
            userService.createUser(info);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") String id){
        try {
            return ResponseEntity.ok(userService.getUser(id));
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<UserPostsDto>> getUserPosts(@PathVariable("id") String id){
        if(id == null || id.isEmpty() || !UUID_REGEX.matcher(id).matches()){
            return ResponseEntity.badRequest().build();
        }
        UUID userId = UUID.fromString(id);
        return ResponseEntity.ok(postService.getUserPosts(userId));
    }

    @GetMapping("/likes")
    public ResponseEntity<List<Post>> getLikedPosts(@RequestParam("userId") String id){
        if(id == null || id.isEmpty() || !UUID_REGEX.matcher(id).matches()){
            return ResponseEntity.badRequest().build();
        }
        UUID userId = UUID.fromString(id);
        return ResponseEntity.ok(postService.getLikedPosts(userId));
    }


}
