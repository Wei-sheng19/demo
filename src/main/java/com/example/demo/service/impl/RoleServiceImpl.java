package com.example.demo.service.impl;

import com.example.demo.dao.PermissionRepository;
import com.example.demo.dao.RoleRepository;
import com.example.demo.dto.PermissionDTO;
import com.example.demo.dto.RoleDTO;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.dao.PermissionGroupRepository;
import com.example.demo.dto.PermissionGroupDTO;
import com.example.demo.entity.PermissionGroup;
import com.example.demo.dto.RoleDTO.ParentRoleDTO;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionGroupRepository permissionGroupRepository;

    @Override
    @Transactional(readOnly = true)
    public RoleDTO getRoleById(Integer id) {
        Role role = roleRepository.findByIdWithPermissions(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + id));
        return convertToDTO(role);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDTO getRoleByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with name: " + name));
        return convertToDTO(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAllWithPermissions().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoleDTO createRole(RoleDTO roleDTO) {
        if (roleRepository.existsByName(roleDTO.name())) {
            throw new IllegalArgumentException("Role name already exists: " + roleDTO.name());
        }
        
        Role role = new Role();
        role.setName(roleDTO.name());
        role.setDescription(roleDTO.description());
        
        // 设置权限（如果提供）
        if (roleDTO.permissions() != null && !roleDTO.permissions().isEmpty()) {
            Set<Permission> permissions = new HashSet<>();
            for (PermissionDTO permissionDTO : roleDTO.permissions()) {
                Permission permission = permissionRepository.findById(permissionDTO.id())
                        .orElseThrow(() -> new IllegalArgumentException("Permission not found with id: " + permissionDTO.id()));
                permissions.add(permission);
            }
            role.setPermissions(permissions);
        }
        
        Role savedRole = roleRepository.save(role);
        return convertToDTO(savedRole);
    }

    @Override
    @Transactional
    public RoleDTO updateRole(Integer id, RoleDTO roleDTO) {
        Role role = roleRepository.findByIdWithPermissions(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + id));
        
        // 检查名称是否被修改且是否已存在
        if (!role.getName().equals(roleDTO.name()) && 
            roleRepository.existsByName(roleDTO.name())) {
            throw new IllegalArgumentException("Role name already exists: " + roleDTO.name());
        }
        
        role.setName(roleDTO.name());
        role.setDescription(roleDTO.description());
        
        // 更新权限（如果提供）
        if (roleDTO.permissions() != null) {
            Set<Permission> permissions = new HashSet<>();
            for (PermissionDTO permissionDTO : roleDTO.permissions()) {
                Permission permission = permissionRepository.findById(permissionDTO.id())
                        .orElseThrow(() -> new IllegalArgumentException("Permission not found with id: " + permissionDTO.id()));
                permissions.add(permission);
            }
            role.setPermissions(permissions);
        }
        
        Role updatedRole = roleRepository.save(role);
        return convertToDTO(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(Integer id) {
        if (!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public RoleDTO assignPermissionsToRole(Integer roleId, Set<Integer> permissionIds) {
        Role role = roleRepository.findByIdWithPermissions(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + roleId));
        
        Set<Permission> currentPermissions = role.getPermissions();
        if (currentPermissions == null) {
            currentPermissions = new HashSet<>();
        }
        
        for (Integer permissionId : permissionIds) {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new IllegalArgumentException("Permission not found with id: " + permissionId));
            currentPermissions.add(permission);
        }
        
        role.setPermissions(currentPermissions);
        Role updatedRole = roleRepository.save(role);
        return convertToDTO(updatedRole);
    }

    @Override
    @Transactional
    public RoleDTO removePermissionsFromRole(Integer roleId, Set<Integer> permissionIds) {
        Role role = roleRepository.findByIdWithPermissions(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + roleId));
        
        Set<Permission> currentPermissions = role.getPermissions();
        if (currentPermissions != null) {
            permissionIds.forEach(permissionId -> {
                currentPermissions.removeIf(permission -> permission.getId().equals(permissionId));
            });
            
            role.setPermissions(currentPermissions);
            Role updatedRole = roleRepository.save(role);
            return convertToDTO(updatedRole);
        }
        
        return convertToDTO(role);
    }

    @Override
    @Transactional
    public RoleDTO assignPermissionGroupsToRole(Integer roleId, Set<Integer> groupIds) {
        Role role = roleRepository.findByIdWithPermissionsAndGroups(roleId)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在: " + roleId));
        
        Set<PermissionGroup> currentGroups = role.getPermissionGroups();
        if (currentGroups == null) {
            currentGroups = new HashSet<>();
        }
        
        for (Integer groupId : groupIds) {
            PermissionGroup group = permissionGroupRepository.findById(groupId)
                    .orElseThrow(() -> new IllegalArgumentException("权限组不存在: " + groupId));
            currentGroups.add(group);
        }
        
        role.setPermissionGroups(currentGroups);
        Role updatedRole = roleRepository.save(role);
        return convertToDTO(updatedRole);
    }

    @Override
    @Transactional
    public RoleDTO removePermissionGroupsFromRole(Integer roleId, Set<Integer> groupIds) {
        Role role = roleRepository.findByIdWithPermissionsAndGroups(roleId)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在: " + roleId));
        
        Set<PermissionGroup> currentGroups = role.getPermissionGroups();
        if (currentGroups != null) {
            groupIds.forEach(groupId -> {
                currentGroups.removeIf(group -> group.getId().equals(groupId));
            });
            
            role.setPermissionGroups(currentGroups);
            Role updatedRole = roleRepository.save(role);
            return convertToDTO(updatedRole);
        }
        
        return convertToDTO(role);
    }

    @Override
    @Transactional
    public RoleDTO setParentRole(Integer roleId, Integer parentRoleId) {
        if (roleId.equals(parentRoleId)) {
            throw new IllegalArgumentException("角色不能将自己设为父角色");
        }
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在: " + roleId));
        
        Role parentRole = roleRepository.findById(parentRoleId)
                .orElseThrow(() -> new IllegalArgumentException("父角色不存在: " + parentRoleId));
        
        // 检查循环依赖
        if (hasCircularDependency(parentRole, roleId)) {
            throw new IllegalArgumentException("设置该父角色会导致循环依赖");
        }
        
        role.setParentRole(parentRole);
        Role updatedRole = roleRepository.save(role);
        return convertToDTO(updatedRole);
    }

    @Override
    @Transactional
    public RoleDTO removeParentRole(Integer roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在: " + roleId));
        
        role.setParentRole(null);
        Role updatedRole = roleRepository.save(role);
        return convertToDTO(updatedRole);
    }
    
    // 检查是否存在循环依赖
    private boolean hasCircularDependency(Role role, Integer childRoleId) {
        if (role == null) {
            return false;
        }
        
        if (role.getId().equals(childRoleId)) {
            return true;
        }
        
        return hasCircularDependency(role.getParentRole(), childRoleId);
    }

    private RoleDTO convertToDTO(Role role) {
        // 转换权限为DTO
        Set<PermissionDTO> permissionDTOs = null;
        if (role.getPermissions() != null) {
            permissionDTOs = role.getPermissions().stream()
                    .map(permission -> new PermissionDTO(
                            permission.getId(),
                            permission.getName(),
                            permission.getDescription(),
                            permission.getResource(),
                            permission.getOperation(),
                            permission.getField(),
                            permission.getAuthority()
                    ))
                    .collect(Collectors.toSet());
        }
        
        // 转换权限组为DTO
        Set<PermissionGroupDTO> groupDTOs = null;
        if (role.getPermissionGroups() != null) {
            groupDTOs = role.getPermissionGroups().stream()
                    .map(group -> new PermissionGroupDTO(
                            group.getId(),
                            group.getName(),
                            group.getDescription(),
                            null // 不包含具体权限，避免循环引用
                    ))
                    .collect(Collectors.toSet());
        }
        
        // 转换父角色为DTO
        ParentRoleDTO parentRoleDTO = null;
        if (role.getParentRole() != null) {
            parentRoleDTO = new ParentRoleDTO(
                    role.getParentRole().getId(),
                    role.getParentRole().getName(),
                    role.getParentRole().getDescription()
            );
        }
        
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