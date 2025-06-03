package com.twitter.demo.entities.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateCommentDto {

    @NotBlank
    private String message;
}
