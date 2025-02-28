package com.example.demo.dto;

import java.util.Set;

public record PermissionGroupDTO(
    Integer id,
    String name,
    String description,
    Set<PermissionDTO> permissions
) {
    public PermissionGroupDTO {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("权限组名称不能为空");
        }
    }
}