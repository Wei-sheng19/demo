package com.example.demo.config;

import com.example.demo.dao.PermissionGroupRepository;
import com.example.demo.dao.PermissionRepository;
import com.example.demo.dao.RoleRepository;
import com.example.demo.entity.Permission;
import com.example.demo.entity.PermissionGroup;
import com.example.demo.entity.Role;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.springframework.beans.factory.annotation.Value;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Configuration
public class PermissionInitializer {

    private static final Logger logger = LoggerFactory.getLogger(PermissionInitializer.class);

    @Autowired
    private PermissionRepository permissionRepository;
    
    @Autowired
    private PermissionGroupRepository permissionGroupRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Value("${app.permission.initialization.enabled:false}")
    private boolean initializationEnabled;
    
    @Value("${app.permission.initialization.force:false}")
    private boolean forceInitialization;

    @Value("${app.permission.initialization.backup:true}")
    private boolean backupBeforeInit;

    @Autowired
    private Environment environment;
    
    @Bean
    public CommandLineRunner initPermissionsAndRoles() {
        return args -> {
            // 检查是否启用初始化
            if (!initializationEnabled) {
                logger.info("权限初始化已禁用");
                return;
            }

            // 检查是否处于开发环境
            boolean isDev = Arrays.asList(environment.getActiveProfiles()).contains("dev");
            
            try {
                // 检查是否已经初始化
                if (isAlreadyInitialized() && !forceInitialization) {
                    logger.info("权限数据已存在，跳过初始化");
                    return;
                }

                // 如果需要备份，先进行备份
                if (backupBeforeInit && !isDev) {
                    backupExistingData();
                }

                // 清理现有数据（如果强制初始化）
                if (forceInitialization) {
                    cleanExistingData();
                }

                // 加载配置文件
                PermissionConfig config = loadPermissionConfig();

                // 事务性地执行初始化
                initializePermissionsAndRolesTransactionally(config);

                logger.info("权限、权限组和角色初始化完成");
                
            } catch (Exception e) {
                logger.error("初始化权限和角色失败: " + e.getMessage(), e);
                if (!isDev) {
                    // 在生产环境中发生错误时恢复备份
                    restoreFromBackup();
                }
                throw e;
            }
        };
    }
    
    @Transactional
    protected void initializePermissionsAndRolesTransactionally(PermissionConfig config) {
        // 1. 初始化权限
        Map<String, Permission> permissionMap = initializePermissions(config.getPermissions());
        
        // 2. 初始化权限组
        Map<String, PermissionGroup> groupMap = initializePermissionGroups(
            config.getPermissionGroups(), 
            permissionMap
        );
        
        // 3. 初始化角色和角色之间的父子关系
        initializeRoles(config.getRoles(), permissionMap, groupMap);
    }

    private PermissionConfig loadPermissionConfig() throws IOException {
        LoaderOptions options = new LoaderOptions();
        Constructor constructor = new Constructor(PermissionConfig.class, options);
        PropertyUtils propertyUtils = constructor.getPropertyUtils();
        propertyUtils.setSkipMissingProperties(true);
        
        Yaml yaml = new Yaml(constructor);
        try (InputStream inputStream = new ClassPathResource("permission-config.yml").getInputStream()) {
            return yaml.load(inputStream);
        }
    }

    private void backupExistingData() {
        // 实现备份逻辑
    }

    private void restoreFromBackup() {
        // 实现恢复逻辑
    }

    private void cleanExistingData() {
        roleRepository.deleteAll();
        permissionGroupRepository.deleteAll();
        permissionRepository.deleteAll();
    }
    
    private boolean isAlreadyInitialized() {
        // 通过检查数据库中是否已有一定数量的记录来判断是否已初始化
        return permissionRepository.count() > 0 && roleRepository.count() > 0;
    }
    
    private Map<String, Permission> initializePermissions(List<PermissionConfig.PermissionEntry> permissionEntries) {
        Map<String, Permission> permissionMap = new HashMap<>();
        
        for (PermissionConfig.PermissionEntry entry : permissionEntries) {
            Permission permission = new Permission();
            permission.setName(entry.getName());
            permission.setDescription(entry.getDescription());
            permission.setResource(entry.getResource());
            permission.setOperation(entry.getOperation());
            permission.setField(entry.getField());
            
            Permission savedPermission = permissionRepository.save(permission);
            permissionMap.put(entry.getName(), savedPermission);
        }
        
        return permissionMap;
    }
    
    private Map<String, PermissionGroup> initializePermissionGroups(
            List<PermissionConfig.PermissionGroupEntry> groupEntries, 
            Map<String, Permission> permissionMap) {
        
        Map<String, PermissionGroup> groupMap = new HashMap<>();
        
        for (PermissionConfig.PermissionGroupEntry entry : groupEntries) {
            PermissionGroup group = new PermissionGroup();
            group.setName(entry.getName());
            group.setDescription(entry.getDescription());
            
            Set<Permission> permissions = new HashSet<>();
            if (entry.getPermissions() != null) {
                for (String permissionName : entry.getPermissions()) {
                    Permission permission = permissionMap.get(permissionName);
                    if (permission != null) {
                        permissions.add(permission);
                    } else {
                        logger.error("Permission not found for group {}: {}", 
                            entry.getName(), permissionName);
                    }
                }
            }
            
            group.setPermissions(permissions);
            PermissionGroup savedGroup = permissionGroupRepository.save(group);
            groupMap.put(entry.getName(), savedGroup);
            
            logger.info("Initialized permission group: {} with {} permissions", 
                entry.getName(), permissions.size());
        }
        
        return groupMap;
    }
    
    private void initializeRoles(
            List<PermissionConfig.RoleEntry> roleEntries,
            Map<String, Permission> permissionMap,
            Map<String, PermissionGroup> groupMap) {
        
        // 第一步：创建所有角色（没有设置父角色）
        Map<String, Role> roleMap = new HashMap<>();
        for (PermissionConfig.RoleEntry entry : roleEntries) {
            Role role = new Role();
            role.setName(entry.getName());
            role.setDescription(entry.getDescription());
            
            // 设置权限
            Set<Permission> permissions = new HashSet<>();
            for (String permissionName : entry.getPermissions()) {
                Permission permission = permissionMap.get(permissionName);
                if (permission != null) {
                    permissions.add(permission);
                }
            }
            role.setPermissions(permissions);
            
            // 设置权限组
            Set<PermissionGroup> groups = new HashSet<>();
            for (String groupName : entry.getPermissionGroups()) {
                PermissionGroup group = groupMap.get(groupName);
                if (group != null) {
                    groups.add(group);
                }
            }
            role.setPermissionGroups(groups);
            
            Role savedRole = roleRepository.save(role);
            roleMap.put(entry.getName(), savedRole);
        }
        
        // 第二步：设置父角色关系
        for (PermissionConfig.RoleEntry entry : roleEntries) {
            if (entry.getParentRole() != null && !entry.getParentRole().isEmpty()) {
                Role role = roleMap.get(entry.getName());
                Role parentRole = roleMap.get(entry.getParentRole());
                
                if (role != null && parentRole != null) {
                    role.setParentRole(parentRole);
                    roleRepository.save(role);
                }
            }
        }
    }
    
    @Data
    public static class PermissionConfig {
        private List<PermissionEntry> permissions;
        private List<PermissionGroupEntry> permissionGroups;
        private List<RoleEntry> roles;
        
        public List<PermissionGroupEntry> getPermission_groups() {
            return permissionGroups;
        }
        
        public void setPermission_groups(List<PermissionGroupEntry> groups) {
            this.permissionGroups = groups;
        }
        
        @Data
        public static class PermissionEntry {
            private String name;
            private String description;
            private String resource;
            private String operation;
            private String field;
        }
        
        @Data
        public static class PermissionGroupEntry {
            private String name;
            private String description;
            private List<String> permissions;
        }
        
        @Data
        public static class RoleEntry {
            private String name;
            private String description;
            private List<String> permissions;
            private List<String> permissionGroups;
            private String parentRole;
            
            public List<String> getPermission_groups() {
                return permissionGroups;
            }
            
            public void setPermission_groups(List<String> groups) {
                this.permissionGroups = groups;
            }
            
            public String getParent_role() {
                return parentRole;
            }
            
            public void setParent_role(String role) {
                this.parentRole = role;
            }
        }
    }
} 