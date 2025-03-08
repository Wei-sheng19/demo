package com.example.demo.dto.auth;

import java.util.Set;
import java.util.stream.Collectors;
import com.example.demo.entity.PermissionGroup;

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

    // 静态工厂方法：从PermissionGroup实体创建PermissionGroupDTO
    public static PermissionGroupDTO fromPermissionGroup(PermissionGroup group) {
        Set<PermissionDTO> permissionDTOs = group.getPermissions().stream()
            .map(PermissionDTO::fromPermission)
            .collect(Collectors.toSet());

        return new PermissionGroupDTO(
            group.getId(),
            group.getName(),
            group.getDescription(),
            permissionDTOs
        );
    }
}