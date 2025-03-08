package com.example.demo.dto.auth;

import java.util.Set;
import java.util.stream.Collectors;
import com.example.demo.entity.Role;

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
    ) {
        // 静态工厂方法：从Role实体创建ParentRoleDTO
        public static ParentRoleDTO fromRole(Role role) {
            return new ParentRoleDTO(
                role.getId(),
                role.getName(),
                role.getDescription()
            );
        }
    }

    // 静态工厂方法：从Role实体创建RoleDTO
    public static RoleDTO fromRole(Role role) {
        Set<PermissionDTO> permissionDTOs = role.getPermissions().stream()
            .map(PermissionDTO::fromPermission)
            .collect(Collectors.toSet());

        Set<PermissionGroupDTO> groupDTOs = role.getPermissionGroups().stream()
            .map(PermissionGroupDTO::fromPermissionGroup)
            .collect(Collectors.toSet());

        ParentRoleDTO parentRoleDTO = role.getParentRole() != null ? 
            ParentRoleDTO.fromRole(role.getParentRole()) : null;

        return new RoleDTO(
            role.getId(),
            role.getName(),
            role.getDescription(),
            permissionDTOs,
            groupDTOs,
            parentRoleDTO
        );
    }
} 