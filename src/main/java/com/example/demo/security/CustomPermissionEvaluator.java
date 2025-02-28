package com.example.demo.security;

import com.example.demo.entity.Permission;
import com.example.demo.entity.PermissionGroup;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.dao.UserRepository;
import com.example.demo.dao.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Component("permissionEvaluator")
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private PermissionExpressionParser expressionParser;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        // This method isn't used but must be implemented
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        // This method isn't used but must be implemented
        return false;
    }
    
    /**
     * Check if the authenticated user has the specified permission
     * 
     * @param authentication the current authentication
     * @param resource the resource to check
     * @param operation the operation to check
     * @param field the field to check
     * @return true if the user has permission, false otherwise
     */
    public boolean hasPermission(Authentication authentication, String resource, String operation, String field) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        // 构造需要检查的权限字符串
        String requiredPermission = String.format("%s:%s:%s", resource, operation, field);
        
        // 检查用户是否具有所需权限
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> expressionParser.matches(requiredPermission, authority));
    }
    
    /**
     * Recursively check if a role or its parents have the required permission
     */
    private boolean checkRoleAndParents(Role role, String requiredPermission) {
        // Load role with permissions and permission groups
        Role loadedRole = roleRepository.findByIdWithPermissionsAndGroups(role.getId())
                .orElse(role);
        
        // Check direct permissions
        if (loadedRole.getPermissions() != null) {
            for (Permission permission : loadedRole.getPermissions()) {
                if (expressionParser.matches(requiredPermission, permission.getAuthority())) {
                    return true;
                }
            }
        }
        
        // Check permissions from permission groups
        if (loadedRole.getPermissionGroups() != null) {
            for (PermissionGroup group : loadedRole.getPermissionGroups()) {
                if (group.getPermissions() != null) {
                    for (Permission permission : group.getPermissions()) {
                        if (expressionParser.matches(requiredPermission, permission.getAuthority())) {
                            return true;
                        }
                    }
                }
            }
        }
        
        // Check parent role if it exists
        if (loadedRole.getParentRole() != null) {
            Role parentRole = roleRepository.findByIdWithParentRole(loadedRole.getParentRole().getId())
                    .orElse(loadedRole.getParentRole());
            return checkRoleAndParents(parentRole, requiredPermission);
        }
        
        return false;
    }
    
    /**
     * Utility method to extract all permission expressions from authorities
     */
    private List<String> extractPermissionExpressions(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.contains(":"))
                .collect(Collectors.toList());
    }
} 