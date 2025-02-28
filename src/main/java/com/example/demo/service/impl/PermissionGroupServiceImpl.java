package com.example.demo.service.impl;

import com.example.demo.dao.PermissionGroupRepository;
import com.example.demo.dao.PermissionRepository;
import com.example.demo.dto.PermissionDTO;
import com.example.demo.dto.PermissionGroupDTO;
import com.example.demo.entity.Permission;
import com.example.demo.entity.PermissionGroup;
import com.example.demo.service.PermissionGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionGroupServiceImpl implements PermissionGroupService {

    @Autowired
    private PermissionGroupRepository permissionGroupRepository;
    
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    @Transactional(readOnly = true)
    public PermissionGroupDTO getPermissionGroupById(Integer id) {
        PermissionGroup group = permissionGroupRepository.findByIdWithPermissions(id)
                .orElseThrow(() -> new IllegalArgumentException("权限组不存在: " + id));
        return convertToDTO(group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionGroupDTO> getAllPermissionGroups() {
        return permissionGroupRepository.findAllWithPermissions().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PermissionGroupDTO createPermissionGroup(PermissionGroupDTO permissionGroupDTO) {
        if (permissionGroupRepository.existsByName(permissionGroupDTO.name())) {
            throw new IllegalArgumentException("权限组名称已存在: " + permissionGroupDTO.name());
        }
        
        PermissionGroup group = new PermissionGroup();
        group.setName(permissionGroupDTO.name());
        group.setDescription(permissionGroupDTO.description());
        
        // 设置权限（如果有）
        if (permissionGroupDTO.permissions() != null && !permissionGroupDTO.permissions().isEmpty()) {
            Set<Permission> permissions = new HashSet<>();
            for (PermissionDTO permissionDTO : permissionGroupDTO.permissions()) {
                Permission permission = permissionRepository.findById(permissionDTO.id())
                        .orElseThrow(() -> new IllegalArgumentException("权限不存在: " + permissionDTO.id()));
                permissions.add(permission);
            }
            group.setPermissions(permissions);
        }
        
        PermissionGroup savedGroup = permissionGroupRepository.save(group);
        return convertToDTO(savedGroup);
    }

    @Override
    @Transactional
    public PermissionGroupDTO updatePermissionGroup(Integer id, PermissionGroupDTO permissionGroupDTO) {
        PermissionGroup group = permissionGroupRepository.findByIdWithPermissions(id)
                .orElseThrow(() -> new IllegalArgumentException("权限组不存在: " + id));
        
        // 检查名称是否被修改且是否已存在
        if (!group.getName().equals(permissionGroupDTO.name()) && 
            permissionGroupRepository.existsByName(permissionGroupDTO.name())) {
            throw new IllegalArgumentException("权限组名称已存在: " + permissionGroupDTO.name());
        }
        
        group.setName(permissionGroupDTO.name());
        group.setDescription(permissionGroupDTO.description());
        
        // 更新权限（如果有）
        if (permissionGroupDTO.permissions() != null) {
            Set<Permission> permissions = new HashSet<>();
            for (PermissionDTO permissionDTO : permissionGroupDTO.permissions()) {
                Permission permission = permissionRepository.findById(permissionDTO.id())
                        .orElseThrow(() -> new IllegalArgumentException("权限不存在: " + permissionDTO.id()));
                permissions.add(permission);
            }
            group.setPermissions(permissions);
        }
        
        PermissionGroup updatedGroup = permissionGroupRepository.save(group);
        return convertToDTO(updatedGroup);
    }

    @Override
    @Transactional
    public void deletePermissionGroup(Integer id) {
        if (!permissionGroupRepository.existsById(id)) {
            throw new IllegalArgumentException("权限组不存在: " + id);
        }
        permissionGroupRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PermissionGroupDTO assignPermissionsToGroup(Integer groupId, Set<Integer> permissionIds) {
        PermissionGroup group = permissionGroupRepository.findByIdWithPermissions(groupId)
                .orElseThrow(() -> new IllegalArgumentException("权限组不存在: " + groupId));
        
        Set<Permission> currentPermissions = group.getPermissions();
        if (currentPermissions == null) {
            currentPermissions = new HashSet<>();
        }
        
        for (Integer permissionId : permissionIds) {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new IllegalArgumentException("权限不存在: " + permissionId));
            currentPermissions.add(permission);
        }
        
        group.setPermissions(currentPermissions);
        PermissionGroup updatedGroup = permissionGroupRepository.save(group);
        return convertToDTO(updatedGroup);
    }

    @Override
    @Transactional
    public PermissionGroupDTO removePermissionsFromGroup(Integer groupId, Set<Integer> permissionIds) {
        PermissionGroup group = permissionGroupRepository.findByIdWithPermissions(groupId)
                .orElseThrow(() -> new IllegalArgumentException("权限组不存在: " + groupId));
        
        Set<Permission> currentPermissions = group.getPermissions();
        if (currentPermissions != null) {
            permissionIds.forEach(permissionId -> {
                currentPermissions.removeIf(permission -> permission.getId().equals(permissionId));
            });
            
            group.setPermissions(currentPermissions);
            PermissionGroup updatedGroup = permissionGroupRepository.save(group);
            return convertToDTO(updatedGroup);
        }
        
        return convertToDTO(group);
    }

    private PermissionGroupDTO convertToDTO(PermissionGroup group) {
        Set<PermissionDTO> permissionDTOs = null;
        if (group.getPermissions() != null) {
            permissionDTOs = group.getPermissions().stream()
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
        
        return new PermissionGroupDTO(
                group.getId(),
                group.getName(),
                group.getDescription(),
                permissionDTOs
        );
    }
} 