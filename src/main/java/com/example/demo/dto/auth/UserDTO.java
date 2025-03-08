package com.example.demo.dto.auth;

import java.util.Set;
import java.util.stream.Collectors;
import com.example.demo.entity.User;

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

    // 静态工厂方法：从User实体创建UserDTO
    public static UserDTO fromUser(User user) {
        Set<RoleDTO> roleDTOs = user.getRoles().stream()
            .map(RoleDTO::fromRole)
            .collect(Collectors.toSet());

        return new UserDTO(
            user.getUserId(),
            user.getUsername(),
            roleDTOs
        );
    }
} 