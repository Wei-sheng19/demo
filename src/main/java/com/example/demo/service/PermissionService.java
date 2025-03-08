package com.example.demo.service;

import com.example.demo.dto.auth.PermissionDTO;
import java.util.List;

public interface PermissionService {
    PermissionDTO getPermissionById(Integer id);
    PermissionDTO getPermissionByName(String name);
    List<PermissionDTO> getAllPermissions();
    PermissionDTO createPermission(PermissionDTO permissionDTO);
    PermissionDTO updatePermission(Integer id, PermissionDTO permissionDTO);
    void deletePermission(Integer id);
} 