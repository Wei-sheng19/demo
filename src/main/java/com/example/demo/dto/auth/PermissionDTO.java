package com.example.demo.dto.auth;

import com.example.demo.entity.Permission;

public record PermissionDTO(
    Integer id,
    String name,
    String description,
    String resource,
    String operation,
    String field,
    String authority
) {
    public PermissionDTO {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Permission name cannot be null or blank");
        }
        if (resource == null || resource.isBlank()) {
            throw new IllegalArgumentException("Resource cannot be null or blank");
        }
        if (operation == null || operation.isBlank()) {
            throw new IllegalArgumentException("Operation cannot be null or blank");
        }
        if (field == null || field.isBlank()) {
            throw new IllegalArgumentException("Field cannot be null or blank");
        }
        
        // 如果authority为null，则自动生成
        if (authority == null) {
            authority = resource + ":" + operation + ":" + field;
        }
    }

    // 静态工厂方法：从Permission实体创建PermissionDTO
    public static PermissionDTO fromPermission(Permission permission) {
        return new PermissionDTO(
            permission.getId(),
            permission.getName(),
            permission.getDescription(),
            permission.getResource(),
            permission.getOperation(),
            permission.getField(),
            permission.getAuthority()
        );
    }
}