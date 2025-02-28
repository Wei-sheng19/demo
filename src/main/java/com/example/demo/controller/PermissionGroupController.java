package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.PermissionGroupDTO;
import com.example.demo.security.RequirePermission;
import com.example.demo.service.PermissionGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/permission-groups")
public class PermissionGroupController {
    
    @Autowired
    private PermissionGroupService permissionGroupService;
    
    @GetMapping
    @RequirePermission(resource = "permission_group", operation = "read", field = "*")
    public ResponseEntity<ApiResponse<?>> getAllPermissionGroups() {
        try {
            List<PermissionGroupDTO> groups = permissionGroupService.getAllPermissionGroups();
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "权限组获取成功", groups));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "获取权限组失败: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/{id}")
    @RequirePermission(resource = "permission_group", operation = "read", field = "*")
    public ResponseEntity<ApiResponse<?>> getPermissionGroupById(@PathVariable Integer id) {
        try {
            PermissionGroupDTO group = permissionGroupService.getPermissionGroupById(id);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "权限组获取成功", group));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "获取权限组失败: " + e.getMessage(), null));
        }
    }
    
    @PostMapping
    @RequirePermission(resource = "permission_group", operation = "create", field = "*")
    public ResponseEntity<ApiResponse<?>> createPermissionGroup(@RequestBody PermissionGroupDTO permissionGroupDTO) {
        try {
            PermissionGroupDTO createdGroup = permissionGroupService.createPermissionGroup(permissionGroupDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("SUCCESS", "权限组创建成功", createdGroup));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "创建权限组失败: " + e.getMessage(), null));
        }
    }
    
    @PutMapping("/{id}")
    @RequirePermission(resource = "permission_group", operation = "update", field = "*")
    public ResponseEntity<ApiResponse<?>> updatePermissionGroup(
            @PathVariable Integer id, 
            @RequestBody PermissionGroupDTO permissionGroupDTO) {
        try {
            PermissionGroupDTO updatedGroup = permissionGroupService.updatePermissionGroup(id, permissionGroupDTO);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "权限组更新成功", updatedGroup));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "更新权限组失败: " + e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/{id}")
    @RequirePermission(resource = "permission_group", operation = "delete", field = "*")
    public ResponseEntity<ApiResponse<?>> deletePermissionGroup(@PathVariable Integer id) {
        try {
            permissionGroupService.deletePermissionGroup(id);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "权限组删除成功", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "删除权限组失败: " + e.getMessage(), null));
        }
    }
    
    @PostMapping("/{id}/permissions")
    @RequirePermission(resource = "permission_group", operation = "update", field = "permissions")
    public ResponseEntity<ApiResponse<?>> assignPermissionsToGroup(
            @PathVariable Integer id, 
            @RequestBody Set<Integer> permissionIds) {
        try {
            PermissionGroupDTO updatedGroup = permissionGroupService.assignPermissionsToGroup(id, permissionIds);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "权限分配成功", updatedGroup));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "分配权限失败: " + e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/{id}/permissions")
    @RequirePermission(resource = "permission_group", operation = "update", field = "permissions")
    public ResponseEntity<ApiResponse<?>> removePermissionsFromGroup(
            @PathVariable Integer id, 
            @RequestBody Set<Integer> permissionIds) {
        try {
            PermissionGroupDTO updatedGroup = permissionGroupService.removePermissionsFromGroup(id, permissionIds);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "权限移除成功", updatedGroup));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "移除权限失败: " + e.getMessage(), null));
        }
    }
} 