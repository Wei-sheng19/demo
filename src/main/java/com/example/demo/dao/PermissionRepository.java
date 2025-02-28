package com.example.demo.dao;

import com.example.demo.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Optional<Permission> findByName(String name);
    
    boolean existsByName(String name);
    
    // 检查权限是否存在（通过三个维度）
    boolean existsByResourceAndOperationAndField(String resource, String operation, String field);
    
    // 根据三个维度查找权限
    Optional<Permission> findByResourceAndOperationAndField(
            @Param("resource") String resource, 
            @Param("operation") String operation, 
            @Param("field") String field);
    
    List<Permission> findByResource(String resource);
    
    List<Permission> findByResourceAndOperation(String resource, String operation);
} 