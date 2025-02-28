package com.example.demo.dao;

import com.example.demo.entity.PermissionGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionGroupRepository extends JpaRepository<PermissionGroup, Integer> {
    boolean existsByName(String name);
    
    Optional<PermissionGroup> findByName(String name);
    
    @EntityGraph(attributePaths = {"permissions"})
    @Query("SELECT pg FROM PermissionGroup pg WHERE pg.id = :id")
    Optional<PermissionGroup> findByIdWithPermissions(@Param("id") Integer id);
    
    @EntityGraph(attributePaths = {"permissions"})
    @Query("SELECT pg FROM PermissionGroup pg")
    List<PermissionGroup> findAllWithPermissions();
} 