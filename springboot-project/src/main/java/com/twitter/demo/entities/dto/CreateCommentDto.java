package com.twitter.demo.entities.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentDto {

    @NotNull
    private UUID userId;

    @NotNull
    private String message;

    @NotNull
    private UUID postId;

}
