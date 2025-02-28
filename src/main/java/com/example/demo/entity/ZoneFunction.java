package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "zone_function")
public class ZoneFunction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "function_id")
    private Integer functionId;  // 功能ID

    @Column(name = "function_name", nullable = false, length = 100)
    private String functionName;  // 功能名称
}
