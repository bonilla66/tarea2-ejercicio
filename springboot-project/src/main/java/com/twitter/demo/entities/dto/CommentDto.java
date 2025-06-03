package com.twitter.demo.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class CommentDto {
    private UUID id;
    private UUID authorId;
    private String authorName;
    private UUID postId;
    private String message;
}
