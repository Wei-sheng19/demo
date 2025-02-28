package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.RoleDTO;
import com.example.demo.security.RequirePermission;
import com.example.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    
    @Autowired
    private RoleService roleService;
    
    @GetMapping
    @RequirePermission(resource = "role", operation = "read", field = "*")
    public ResponseEntity<ApiResponse<?>> getAllRoles() {
        try {
            List<RoleDTO> roles = roleService.getAllRoles();
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "角色获取成功", roles));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "获取角色失败: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/{id}")
    @RequirePermission(resource = "role", operation = "read", field = "*")
    public ResponseEntity<ApiResponse<?>> getRoleById(@PathVariable Integer id) {
        try {
            RoleDTO role = roleService.getRoleById(id);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "角色获取成功", role));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "获取角色失败: " + e.getMessage(), null));
        }
    }
    
    @PostMapping
    @RequirePermission(resource = "role", operation = "create", field = "*")
    public ResponseEntity<ApiResponse<?>> createRole(@RequestBody RoleDTO roleDTO) {
        try {
            RoleDTO createdRole = roleService.createRole(roleDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("SUCCESS", "角色创建成功", createdRole));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "创建角色失败: " + e.getMessage(), null));
        }
    }
    
    @PutMapping("/{id}")
    @RequirePermission(resource = "role", operation = "update", field = "*")
    public ResponseEntity<ApiResponse<?>> updateRole(@PathVariable Integer id, @RequestBody RoleDTO roleDTO) {
        try {
            RoleDTO updatedRole = roleService.updateRole(id, roleDTO);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "角色更新成功", updatedRole));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "更新角色失败: " + e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/{id}")
    @RequirePermission(resource = "role", operation = "delete", field = "*")
    public ResponseEntity<ApiResponse<?>> deleteRole(@PathVariable Integer id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "角色删除成功", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "删除角色失败: " + e.getMessage(), null));
        }
    }
    
    @PostMapping("/{id}/permissions")
    @RequirePermission(resource = "role", operation = "update", field = "permissions")
    public ResponseEntity<ApiResponse<?>> assignPermissionsToRole(
            @PathVariable Integer id, 
            @RequestBody Set<Integer> permissionIds) {
        try {
            RoleDTO updatedRole = roleService.assignPermissionsToRole(id, permissionIds);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "权限分配成功", updatedRole));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "分配权限失败: " + e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/{id}/permissions")
    @RequirePermission(resource = "role", operation = "update", field = "permissions")
    public ResponseEntity<ApiResponse<?>> removePermissionsFromRole(
            @PathVariable Integer id, 
            @RequestBody Set<Integer> permissionIds) {
        try {
            RoleDTO updatedRole = roleService.removePermissionsFromRole(id, permissionIds);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "权限移除成功", updatedRole));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "移除权限失败: " + e.getMessage(), null));
        }
    }
    
    @PostMapping("/{id}/permission-groups")
    @RequirePermission(resource = "role", operation = "update", field = "permission_groups")
    public ResponseEntity<ApiResponse<?>> assignPermissionGroupsToRole(
            @PathVariable Integer id, 
            @RequestBody Set<Integer> groupIds) {
        try {
            RoleDTO updatedRole = roleService.assignPermissionGroupsToRole(id, groupIds);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "权限组分配成功", updatedRole));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "分配权限组失败: " + e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/{id}/permission-groups")
    @RequirePermission(resource = "role", operation = "update", field = "permission_groups")
    public ResponseEntity<ApiResponse<?>> removePermissionGroupsFromRole(
            @PathVariable Integer id, 
            @RequestBody Set<Integer> groupIds) {
        try {
            RoleDTO updatedRole = roleService.removePermissionGroupsFromRole(id, groupIds);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "权限组移除成功", updatedRole));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "移除权限组失败: " + e.getMessage(), null));
        }
    }
    
    @PostMapping("/{id}/parent-role/{parentId}")
    @RequirePermission(resource = "role", operation = "update", field = "parent_role")
    public ResponseEntity<ApiResponse<?>> setParentRole(
            @PathVariable Integer id, 
            @PathVariable Integer parentId) {
        try {
            RoleDTO updatedRole = roleService.setParentRole(id, parentId);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "父角色设置成功", updatedRole));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "设置父角色失败: " + e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/{id}/parent-role")
    @RequirePermission(resource = "role", operation = "update", field = "parent_role")
    public ResponseEntity<ApiResponse<?>> removeParentRole(@PathVariable Integer id) {
        try {
            RoleDTO updatedRole = roleService.removeParentRole(id);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "父角色移除成功", updatedRole));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "移除父角色失败: " + e.getMessage(), null));
        }
    }
} 