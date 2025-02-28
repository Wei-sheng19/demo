package com.example.demo.service;

import com.example.demo.dto.PermissionGroupDTO;
import java.util.List;
import java.util.Set;

public interface PermissionGroupService {
    PermissionGroupDTO getPermissionGroupById(Integer id);
    List<PermissionGroupDTO> getAllPermissionGroups();
    PermissionGroupDTO createPermissionGroup(PermissionGroupDTO permissionGroupDTO);
    PermissionGroupDTO updatePermissionGroup(Integer id, PermissionGroupDTO permissionGroupDTO);
    void deletePermissionGroup(Integer id);
    PermissionGroupDTO assignPermissionsToGroup(Integer groupId, Set<Integer> permissionIds);
    PermissionGroupDTO removePermissionsFromGroup(Integer groupId, Set<Integer> permissionIds);
} 