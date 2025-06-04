package com.twitter.demo.services;

import com.twitter.demo.entities.Post;
import com.twitter.demo.entities.User;
import com.twitter.demo.entities.dto.*;
import com.twitter.demo.repositories.PostRepository;
import com.twitter.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public void createPost(CreatePostDto request){
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }

        Post post = new Post();
        post.setAuthor(optionalUser.get());
        post.setMessage(request.getContent());
        post.setComments(new ArrayList<>());
        postRepository.save(post);
    }

    public List<UserPostsDto> getUserPosts(UUID userId){
        List<UserPostsDto> response = new ArrayList<>();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        List<Post> userPost = postRepository.findAllByAuthor(optionalUser.get());
        userPost.forEach(post -> {
            response.add(new UserPostsDto(post.getId(), post.getMessage()));
        });

        return response;
    }

    public void likePost(LikeDto dto){
        UUID userId = dto.getUserId();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();

        UUID postId = dto.getPostId();
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()){
            throw new RuntimeException("Post not found");
        }
        Post post = optionalPost.get();

        boolean isLiked = post.getLikes().stream().anyMatch(
                u -> u.getId().equals(userId));

        if (isLiked){
            throw new RuntimeException("Post is already liked");
        }

        post.getLikes().add(user);
        postRepository.save(post);
    }

    public List<Post> getLikedPosts(UUID userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        return optionalUser.get().getLikes();
    }

    public List<UserPostsDto> getAllPosts(){
        List<Post> posts = postRepository.findAll();
        List<UserPostsDto> response = new ArrayList<>();
        posts.forEach(post -> {
            response.add(new UserPostsDto(post.getId(), post.getMessage()));
        });
        return response;
    }

    public List<PostLikesDto> getLikedPosts(){
        List<Post> allposts = postRepository.findAll();
        List<PostLikesDto> response = new ArrayList<>();

        allposts.forEach(post -> {
            if (post.getLikes() != null && !post.getLikes().isEmpty()){
                List<UserDto> usersliked = new ArrayList<>();
                post.getLikes().forEach(user -> {
                    usersliked.add(new UserDto(user.getId(), user.getName(), user.getEmail()));
                });

                response.add(new PostLikesDto(post.getId(), usersliked));
            }
        });

        return response;
    }

    public void removeLike(UUID userId, UUID postId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()){
            throw new RuntimeException("Post not found");
        }

        Post post = optionalPost.get();

        if (post.getLikes().contains(user)){
            post.getLikes().remove(user);
            postRepository.save(post);
        } else {
            throw new RuntimeException("User hasn't like this post");
        }
    }



}
