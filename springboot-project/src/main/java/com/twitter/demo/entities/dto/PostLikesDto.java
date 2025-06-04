package com.twitter.demo.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostLikesDto {

    private UUID postId;
    private List<UserDto> likedUsers;
}
