package com.example.demo.dto.auth;

public record LoginRequestDTO(
    String username,
    String password
) {
    public LoginRequestDTO {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
    }
} 