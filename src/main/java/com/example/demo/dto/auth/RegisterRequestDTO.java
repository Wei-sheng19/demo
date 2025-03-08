package com.example.demo.dto.auth;

import java.util.Set;

public record RegisterRequestDTO(
    String username,
    String password,
    Set<Integer> roleIds
) {
    public RegisterRequestDTO {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
    }
} 