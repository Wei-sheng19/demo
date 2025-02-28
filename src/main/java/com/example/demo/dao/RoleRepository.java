package com.example.demo.dao;

import com.example.demo.entity.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    boolean existsByName(String name);
    
    Optional<Role> findByName(String name);
    
    @EntityGraph(attributePaths = {"permissions"})
    @Query("SELECT r FROM Role r WHERE r.id = :id")
    Optional<Role> findByIdWithPermissions(@Param("id") Integer id);
    
    @EntityGraph(attributePaths = {"permissions", "permissionGroups", "permissionGroups.permissions"})
    @Query("SELECT r FROM Role r WHERE r.id = :id")
    Optional<Role> findByIdWithPermissionsAndGroups(@Param("id") Integer id);
    
    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.parentRole WHERE r.id = :id")
    Optional<Role> findByIdWithParentRole(@Param("id") Integer id);
    
    // 添加缺少的方法 - 获取所有角色及其权限
    @EntityGraph(attributePaths = {"permissions"})
    List<Role> findAll();
    
    // 显式定义findAllWithPermissions方法
    @EntityGraph(attributePaths = {"permissions"})
    @Query("SELECT r FROM Role r")
    List<Role> findAllWithPermissions();
}