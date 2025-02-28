package com.example.demo.dao;

import com.example.demo.entity.User;
import com.example.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // 根据用户名查找用户（用于登录验证）
    Optional<User> findByUsername(String username);
    
    // 检查用户名是否已存在（用于注册验证）
    boolean existsByUsername(String username);
    
    // 根据创建时间范围查找用户
    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // 根据用户名模糊查询
    List<User> findByUsernameContaining(String username);
    
    // 添加新的方法
    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.userId = :userId")
    Optional<User> findByIdWithRoles(@Param("userId") Integer userId);
    
    @Query("SELECT DISTINCT u FROM User u " +
           "LEFT JOIN FETCH u.roles r " +
           "LEFT JOIN FETCH r.permissions " +
           "LEFT JOIN FETCH r.permissionGroups pg " +
           "LEFT JOIN FETCH pg.permissions " +
           "LEFT JOIN FETCH r.parentRole pr " +
           "LEFT JOIN FETCH pr.permissions " +
           "LEFT JOIN FETCH pr.permissionGroups ppg " +
           "LEFT JOIN FETCH ppg.permissions " +
           "WHERE u.username = :username")
    Optional<User> findByUsernameWithRoles(@Param("username") String username);
    
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);
    
    // 修改为使用roles关联
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role AND u.createdAt > :date")
    List<User> findActiveUsersByRole(@Param("role") Role role, @Param("date") LocalDateTime date);
    
    // 修改为使用roles关联进行统计
    @Query("SELECT r.name, COUNT(u) FROM User u JOIN u.roles r GROUP BY r.name")
    List<Object[]> countByRoleName();
}