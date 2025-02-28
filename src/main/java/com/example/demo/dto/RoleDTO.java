package com.example.demo.dto;

import java.util.Set;

public record RoleDTO(
    Integer id,
    String name,
    String description,
    Set<PermissionDTO> permissions,
    Set<PermissionGroupDTO> permissionGroups,
    ParentRoleDTO parentRole
) {
    public RoleDTO {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("角色名称不能为空");
        }
    }
    
    // 简化的父角色DTO，只包含基本信息
    public record ParentRoleDTO(
        Integer id,
        String name,
        String description
    ) {}
} 