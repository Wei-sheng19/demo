package com.example.demo.service.impl;

import com.example.demo.dto.auth.UserDTO;
import com.example.demo.dao.UserRepository; // 确保导入了 UserRepository
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // 添加 @Service 注解
import com.example.demo.dto.auth.LoginRequestDTO;
import com.example.demo.dto.auth.LoginResponseDTO;
import com.example.demo.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.demo.dto.auth.RegisterRequestDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.dao.RoleRepository;
import java.util.HashSet;
import java.util.Set;
import com.example.demo.entity.Role;
import com.example.demo.entity.Permission;
import com.example.demo.dto.auth.RoleDTO;
import com.example.demo.dto.auth.PermissionDTO;
import com.example.demo.dto.auth.PermissionGroupDTO;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.entity.PermissionGroup;
@Service // 确保使用 @Service 注解
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository; // 注入 UserRepository

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO getUserInfo(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        return convertToDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            log.debug("Attempting login for user: {}", loginRequest.username());
            
            // 查找用户
            User user = userRepository.findByUsername(loginRequest.username())
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + loginRequest.username()));
            
            // 验证密码
            if (!passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
                throw new IllegalArgumentException("Invalid password");
            }
            
            // 收集所有权限
            Set<String> authorities = new HashSet<>();
            if (user.getRoles() != null) {
                for (Role role : user.getRoles()) {
                    // 添加角色
                    authorities.add("ROLE_" + role.getName());
                    
                    // 添加直接权限
                    if (role.getPermissions() != null) {
                        for (Permission permission : role.getPermissions()) {
                            authorities.add(permission.getAuthority());
                        }
                    }
                    
                    // 添加权限组中的权限
                    if (role.getPermissionGroups() != null) {
                        for (PermissionGroup group : role.getPermissionGroups()) {
                            if (group.getPermissions() != null) {
                                for (Permission permission : group.getPermissions()) {
                                    authorities.add(permission.getAuthority());
                                }
                            }
                        }
                    }
                    
                    // 递归添加父角色的权限
                    Role currentRole = role;
                    while (currentRole.getParentRole() != null) {
                        currentRole = currentRole.getParentRole();
                        if (currentRole.getPermissions() != null) {
                            for (Permission permission : currentRole.getPermissions()) {
                                authorities.add(permission.getAuthority());
                            }
                        }
                        if (currentRole.getPermissionGroups() != null) {
                            for (PermissionGroup group : currentRole.getPermissionGroups()) {
                                if (group.getPermissions() != null) {
                                    for (Permission permission : group.getPermissions()) {
                                        authorities.add(permission.getAuthority());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            String token = jwtUtil.generateToken(
                user.getUserId(),
                user.getUsername(),
                authorities
            );
            
            return new LoginResponseDTO(token, convertToDTO(user));
        } catch (Exception e) {
            log.error("Login error for user: " + loginRequest.username(), e);
            throw e;
        }
    }

    @Override
    public UserDTO register(RegisterRequestDTO registerRequest) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(registerRequest.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(registerRequest.username());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.password()));
        user.setCreatedAt(LocalDateTime.now());
        
        // 设置角色
        Set<Role> roles = new HashSet<>();
        if (registerRequest.roleIds() != null && !registerRequest.roleIds().isEmpty()) {
            for (Integer roleId : registerRequest.roleIds()) {
                Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + roleId));
                roles.add(role);
            }
        } else {
            // 如果没有指定角色，可以分配默认角色
            Role defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalArgumentException("Default role 'USER' not found"));
            roles.add(defaultRole);
        }
        user.setRoles(roles);
        
        // 保存用户
        user = userRepository.save(user);
        
        return convertToDTO(user);
    }

    @Override
    public UserDTO assignRolesToUser(Integer userId, Set<Integer> roleIds) {
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        Set<Role> currentRoles = user.getRoles();
        if (currentRoles == null) {
            currentRoles = new HashSet<>();
        }
        
        for (Integer roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + roleId));
            currentRoles.add(role);
        }
        
        user.setRoles(currentRoles);
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    @Override
    public UserDTO removeRolesFromUser(Integer userId, Set<Integer> roleIds) {
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        Set<Role> currentRoles = user.getRoles();
        if (currentRoles != null) {
            roleIds.forEach(roleId -> {
                currentRoles.removeIf(role -> role.getId().equals(roleId));
            });
            
            user.setRoles(currentRoles);
            User updatedUser = userRepository.save(user);
            return convertToDTO(updatedUser);
        }
        
        return convertToDTO(user);
    }

    @Override
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    public UserDTO updateUser(Integer userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        // 检查用户名是否被修改且是否已存在
        if (!user.getUsername().equals(userDTO.username()) && 
            userRepository.existsByUsername(userDTO.username())) {
            throw new IllegalArgumentException("Username already exists: " + userDTO.username());
        }
        
        user.setUsername(userDTO.username());
        
        // 更新角色（如果提供）
        if (userDTO.roles() != null) {
            Set<Role> roles = new HashSet<>();
            for (RoleDTO roleDTO : userDTO.roles()) {
                Role role = roleRepository.findById(roleDTO.id())
                        .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + roleDTO.id()));
                roles.add(role);
            }
            user.setRoles(roles);
        }
        
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    @Override
    public List<UserDTO> getUsersByRoleName(String roleName) {
        return userRepository.findByRoleName(roleName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        Set<RoleDTO> roleDTOs = null;
        if (user.getRoles() != null) {
            roleDTOs = user.getRoles().stream()
                    .map(role -> {
                        // 转换直接权限为DTO
                        Set<PermissionDTO> permissionDTOs = new HashSet<>();
                        if (role.getPermissions() != null) {
                            permissionDTOs.addAll(role.getPermissions().stream()
                                    .map(PermissionDTO::fromPermission)
                                    .collect(Collectors.toSet()));
                        }
                        
                        // 转换权限组为DTO，并包含权限组中的权限
                        Set<PermissionGroupDTO> groupDTOs = null;
                        if (role.getPermissionGroups() != null) {
                            groupDTOs = role.getPermissionGroups().stream()
                                    .map(group -> {
                                        Set<PermissionDTO> groupPermissionDTOs = null;
                                        if (group.getPermissions() != null) {
                                            groupPermissionDTOs = group.getPermissions().stream()
                                                    .map(PermissionDTO::fromPermission)
                                                    .collect(Collectors.toSet());
                                        }
                                        return new PermissionGroupDTO(
                                            group.getId(),
                                            group.getName(),
                                            group.getDescription(),
                                            groupPermissionDTOs
                                        );
                                    })
                                    .collect(Collectors.toSet());
                        }
                        
                        // 转换父角色
                        RoleDTO.ParentRoleDTO parentRoleDTO = null;
                        if (role.getParentRole() != null) {
                            parentRoleDTO = new RoleDTO.ParentRoleDTO(
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
                    })
                    .collect(Collectors.toSet());
        }
        
        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                roleDTOs
        );
    }
}