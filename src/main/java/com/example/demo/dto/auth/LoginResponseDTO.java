package com.example.demo.dto.auth;

public record LoginResponseDTO(
    String token,
    UserDTO user
) {
    public LoginResponseDTO {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token cannot be null or blank");
        }
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
    }
} 