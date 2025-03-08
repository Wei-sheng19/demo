package com.example.demo.service;

import com.example.demo.dto.auth.RoleDTO;
import java.util.List;
import java.util.Set;

public interface RoleService {
    RoleDTO getRoleById(Integer id);
    RoleDTO getRoleByName(String name);
    List<RoleDTO> getAllRoles();
    RoleDTO createRole(RoleDTO roleDTO);
    RoleDTO updateRole(Integer id, RoleDTO roleDTO);
    void deleteRole(Integer id);
    
    // 权限管理
    RoleDTO assignPermissionsToRole(Integer roleId, Set<Integer> permissionIds);
    RoleDTO removePermissionsFromRole(Integer roleId, Set<Integer> permissionIds);
    
    // 权限组管理
    RoleDTO assignPermissionGroupsToRole(Integer roleId, Set<Integer> groupIds);
    RoleDTO removePermissionGroupsFromRole(Integer roleId, Set<Integer> groupIds);
    
    // 父角色管理
    RoleDTO setParentRole(Integer roleId, Integer parentRoleId);
    RoleDTO removeParentRole(Integer roleId);
} 