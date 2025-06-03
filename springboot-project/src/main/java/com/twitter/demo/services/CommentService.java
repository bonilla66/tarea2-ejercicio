package com.twitter.demo.services;

import com.twitter.demo.entities.Comments;
import com.twitter.demo.entities.Post;
import com.twitter.demo.entities.User;
import com.twitter.demo.entities.dto.CommentDto;
import com.twitter.demo.entities.dto.CreateCommentDto;
import com.twitter.demo.repositories.CommentsRepository;
import com.twitter.demo.repositories.PostRepository;
import com.twitter.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public CommentDto createComment(CreateCommentDto dto) {
        Optional<User> optionalUser = userRepository.findById(dto.getUserId());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User author = optionalUser.get();

        Optional<Post> optionalPost = postRepository.findById(dto.getPostId());
        if (optionalPost.isEmpty()) {
            throw new RuntimeException("Post not found");
        }
        Post post = optionalPost.get();

        Comments comment = new Comments();
        comment.setAuthor(author);
        comment.setPost(post);
        comment.setMessage(dto.getMessage());
        Comments saved = commentsRepository.save(comment);

        return new CommentDto(saved.getId(), author.getId(), author.getName(), post.getId(), saved.getMessage() );
    }


    public List<CommentDto> getCommentsByPost(UUID postId) {
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("Post not found");
        }
        List<Comments> comments = commentsRepository.findAllByPostId(postId);
        List<CommentDto> result = new ArrayList<>();
        comments.forEach(com -> result.add(new CommentDto(com.getId(), com.getAuthor().getId(), com.getAuthor().getName(), com.getPost().getId(), com.getMessage() )));
        return result;
    }

    public List<CommentDto> getCommentsByUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        List<Comments> comments = commentsRepository.findByAuthorId(userId);
        List<CommentDto> response = new ArrayList<>();
        comments.forEach(com -> response.add(new CommentDto(com.getId(), com.getAuthor().getId(), com.getAuthor().getName(), com.getPost().getId(), com.getMessage())));
        return response;
    }


    public CommentDto updateComment(UUID commentId, String newMessage) {
        Optional<Comments> optionalComment = commentsRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new RuntimeException("Comment not found");
        }
        Comments comment = optionalComment.get();
        comment.setMessage(newMessage);
        Comments updated = commentsRepository.save(comment);

        return new CommentDto(updated.getId(), updated.getAuthor().getId(), updated.getAuthor().getName(), updated.getPost().getId(), updated.getMessage());
    }

    public void deleteComment(UUID commentId) {
        if (!commentsRepository.existsById(commentId)) {
            throw new RuntimeException("Comment not found");
        }
        commentsRepository.deleteById(commentId);
    }

    public CommentDto getCommentById(UUID commentId) {
        Optional<Comments> optionalComments = commentsRepository.findById(commentId);
        if (optionalComments.isEmpty()) {
            throw new RuntimeException("Comment not found");
        }
        Comments com = optionalComments.get();
        return new CommentDto(com.getId(), com.getAuthor().getId(), com.getAuthor().getName(), com.getPost().getId(), com.getMessage());
    }

    public List<Comments> getAllComments() {
        return commentsRepository.findAll();
    }
}
