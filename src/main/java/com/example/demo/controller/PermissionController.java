package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.PermissionDTO;
import com.example.demo.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.security.RequirePermission;
import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    
    @Autowired
    private PermissionService permissionService;
    
    @GetMapping("/{id}")
    @RequirePermission(resource = "permission", operation = "read", field = "*")
    public ResponseEntity<ApiResponse<?>> getPermissionById(@PathVariable Integer id) {
        try {
            PermissionDTO permission = permissionService.getPermissionById(id);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Permission retrieved successfully", permission));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "Failed to retrieve permission: " + e.getMessage(), null));
        }
    }
    
    @GetMapping
    @RequirePermission(resource = "permission", operation = "read", field = "*")
    public ResponseEntity<ApiResponse<?>> getAllPermissions() {
        try {
            List<PermissionDTO> permissions = permissionService.getAllPermissions();
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Permissions retrieved successfully", permissions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "Failed to retrieve permissions: " + e.getMessage(), null));
        }
    }
    
    @PostMapping
    @RequirePermission(resource = "permission", operation = "create", field = "*")
    public ResponseEntity<ApiResponse<?>> createPermission(@RequestBody PermissionDTO permissionDTO) {
        try {
            PermissionDTO createdPermission = permissionService.createPermission(permissionDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("SUCCESS", "Permission created successfully", createdPermission));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "Failed to create permission: " + e.getMessage(), null));
        }
    }
    
    @PutMapping("/{id}")
    @RequirePermission(resource = "permission", operation = "update", field = "*")
    public ResponseEntity<ApiResponse<?>> updatePermission(@PathVariable Integer id, @RequestBody PermissionDTO permissionDTO) {
        try {
            PermissionDTO updatedPermission = permissionService.updatePermission(id, permissionDTO);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Permission updated successfully", updatedPermission));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "Failed to update permission: " + e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/{id}")
    @RequirePermission(resource = "permission", operation = "delete", field = "*")
    public ResponseEntity<ApiResponse<?>> deletePermission(@PathVariable Integer id) {
        try {
            permissionService.deletePermission(id);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Permission deleted successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "Failed to delete permission: " + e.getMessage(), null));
        }
    }
} 