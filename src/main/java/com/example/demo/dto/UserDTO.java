package com.example.demo.dto;

import java.util.Set;

public record UserDTO(
        Integer userId,
        String username,
        Set<RoleDTO> roles
) {
    public UserDTO {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
    }
} 