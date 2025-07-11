package com.twitter.demo.entities.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}


