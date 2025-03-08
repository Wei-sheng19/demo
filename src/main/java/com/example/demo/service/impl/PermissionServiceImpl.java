package com.example.demo.service.impl;

import com.example.demo.dao.PermissionRepository;
import com.example.demo.dto.auth.PermissionDTO;
import com.example.demo.entity.Permission;
import com.example.demo.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    @Transactional(readOnly = true)
    public PermissionDTO getPermissionById(Integer id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found with id: " + id));
        return convertToDTO(permission);
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionDTO getPermissionByName(String name) {
        Permission permission = permissionRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found with name: " + name));
        return convertToDTO(permission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PermissionDTO createPermission(PermissionDTO permissionDTO) {
        if (permissionRepository.existsByName(permissionDTO.name())) {
            throw new IllegalArgumentException("Permission name already exists: " + permissionDTO.name());
        }
        
        // 检查权限标识符是否已存在
        String authority = permissionDTO.resource() + ":" + permissionDTO.operation() + ":" + permissionDTO.field();
        if (permissionRepository.existsByResourceAndOperationAndField(
                permissionDTO.resource(), permissionDTO.operation(), permissionDTO.field())) {
            throw new IllegalArgumentException("Permission already exists with authority: " + authority);
        }
        
        Permission permission = new Permission();
        permission.setName(permissionDTO.name());
        permission.setDescription(permissionDTO.description());
        permission.setResource(permissionDTO.resource());
        permission.setOperation(permissionDTO.operation());
        permission.setField(permissionDTO.field());
        
        Permission savedPermission = permissionRepository.save(permission);
        return convertToDTO(savedPermission);
    }

    @Override
    @Transactional
    public PermissionDTO updatePermission(Integer id, PermissionDTO permissionDTO) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found with id: " + id));
        
        // 检查名称是否被修改且是否已存在
        if (!permission.getName().equals(permissionDTO.name()) && 
            permissionRepository.existsByName(permissionDTO.name())) {
            throw new IllegalArgumentException("Permission name already exists: " + permissionDTO.name());
        }
        
        // 检查权限标识符是否被修改且是否已存在
        String newAuthority = permissionDTO.resource() + ":" + permissionDTO.operation() + ":" + permissionDTO.field();
        String oldAuthority = permission.getAuthority();
        
        if (!oldAuthority.equals(newAuthority) &&
            permissionRepository.existsByResourceAndOperationAndField(
                permissionDTO.resource(), permissionDTO.operation(), permissionDTO.field())) {
            throw new IllegalArgumentException("Permission already exists with authority: " + newAuthority);
        }
        
        permission.setName(permissionDTO.name());
        permission.setDescription(permissionDTO.description());
        permission.setResource(permissionDTO.resource());
        permission.setOperation(permissionDTO.operation());
        permission.setField(permissionDTO.field());
        
        Permission updatedPermission = permissionRepository.save(permission);
        return convertToDTO(updatedPermission);
    }

    @Override
    @Transactional
    public void deletePermission(Integer id) {
        if (!permissionRepository.existsById(id)) {
            throw new IllegalArgumentException("Permission not found with id: " + id);
        }
        permissionRepository.deleteById(id);
    }

    private PermissionDTO convertToDTO(Permission permission) {
        return new PermissionDTO(
                permission.getId(),
                permission.getName(),
                permission.getDescription(),
                permission.getResource(),
                permission.getOperation(),
                permission.getField(),
                permission.getAuthority()
        );
    }
}