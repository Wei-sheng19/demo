package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "permission")
public class Permission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, unique = true)
    private String name;  // 权限名称
    
    @Column(nullable = false)
    private String description;  // 权限描述
    
    @Column(nullable = false)
    private String resource;  // 资源类型，如room, maintenance, construction
    
    @Column(nullable = false)
    private String operation;  // 操作类型，如read, edit, create, delete
    
    @Column(nullable = false)
    private String field;  // 字段范围，如basic_info, audit_info, * (all fields)
    
    // 获取完整的权限表达式 (resource:operation:field)
    public String getAuthority() {
        return resource + ":" + operation + ":" + field;
    }
} 