package com.example.demo.security;

import com.example.demo.dao.UserRepository;
import com.example.demo.entity.Permission;
import com.example.demo.entity.PermissionGroup;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Set<GrantedAuthority> authorities = new HashSet<>();
        
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                // 添加角色
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
                
                // 添加角色直接的权限
                if (role.getPermissions() != null) {
                    for (Permission permission : role.getPermissions()) {
                        authorities.add(new SimpleGrantedAuthority(permission.getAuthority()));
                    }
                }
                
                // 添加权限组中的权限
                if (role.getPermissionGroups() != null) {
                    for (PermissionGroup group : role.getPermissionGroups()) {
                        if (group.getPermissions() != null) {
                            for (Permission permission : group.getPermissions()) {
                                authorities.add(new SimpleGrantedAuthority(permission.getAuthority()));
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
                            authorities.add(new SimpleGrantedAuthority(permission.getAuthority()));
                        }
                    }
                    if (currentRole.getPermissionGroups() != null) {
                        for (PermissionGroup group : currentRole.getPermissionGroups()) {
                            if (group.getPermissions() != null) {
                                for (Permission permission : group.getPermissions()) {
                                    authorities.add(new SimpleGrantedAuthority(permission.getAuthority()));
                                }
                            }
                        }
                    }
                }
            }
        }

        logger.debug("User {} has authorities: {}", username, authorities);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                authorities
        );
    }
} 