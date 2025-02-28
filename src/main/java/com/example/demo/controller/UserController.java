package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.*;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.demo.security.RequirePermission;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{userId}")
    @RequirePermission(resource = "user", operation = "read", field = "basic_info")
    public ResponseEntity<ApiResponse<?>> getUserInfo(@PathVariable Integer userId) {
        try {
            UserDTO user = userService.getUserInfo(userId);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "User information retrieved successfully", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "Failed to retrieve user: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/allUsers")
    @RequirePermission(resource = "user", operation = "read", field = "basic_info")
    public ResponseEntity<ApiResponse<?>> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Users retrieved successfully",
                users
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }
    
    @GetMapping("/byRole/{roleName}")
    @RequirePermission(resource = "user", operation = "read", field = "basic_info")
    public ResponseEntity<ApiResponse<?>> getUsersByRoleName(@PathVariable String roleName) {
        try {
            List<UserDTO> users = userService.getUsersByRoleName(roleName);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Users retrieved successfully", users));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "Failed to retrieve users: " + e.getMessage(), null));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            LoginResponseDTO response = userService.login(loginRequest);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Login successful", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "Login failed: " + e.getMessage(), null));
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody RegisterRequestDTO registerRequest) {
        try {
            UserDTO user = userService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("SUCCESS", "User registered successfully", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "Registration failed: " + e.getMessage(), null));
        }
    }
    
    @PutMapping("/{userId}")
    @RequirePermission(resource = "user", operation = "update", field = "basic_info")
    public ResponseEntity<ApiResponse<?>> updateUser(@PathVariable Integer userId, @RequestBody UserDTO userDTO) {
        try {
            if (!userId.equals(userDTO.userId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("ERROR", "User ID in path and body must match", null));
            }
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (userId.toString().equals(auth.getPrincipal().toString()) && 
                !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                if (userDTO.roles() != null) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ApiResponse<>("ERROR", "You cannot modify your own roles", null));
                }
            }
            
            UserDTO updatedUser = userService.updateUser(userId, userDTO);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "User updated successfully", updatedUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "Failed to update user: " + e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/{userId}")
    @RequirePermission(resource = "user", operation = "delete", field = "*")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable Integer userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "User deleted successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "Failed to delete user: " + e.getMessage(), null));
        }
    }
    
    @PostMapping("/{userId}/roles")
    @RequirePermission(resource = "user", operation = "update", field = "roles")
    public ResponseEntity<ApiResponse<?>> assignRolesToUser(
            @PathVariable Integer userId, @RequestBody Set<Integer> roleIds) {
        try {
            UserDTO updatedUser = userService.assignRolesToUser(userId, roleIds);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Roles assigned successfully", updatedUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "Failed to assign roles: " + e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/{userId}/roles")
    @RequirePermission(resource = "user", operation = "update", field = "roles")
    public ResponseEntity<ApiResponse<?>> removeRolesFromUser(
            @PathVariable Integer userId, @RequestBody Set<Integer> roleIds) {
        try {
            UserDTO updatedUser = userService.removeRolesFromUser(userId, roleIds);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Roles removed successfully", updatedUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "Failed to remove roles: " + e.getMessage(), null));
        }
    }
} 