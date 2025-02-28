package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "campus")  // 显式指定表名，避免与数据库关键字冲突
public class Campus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campus_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(name = "floor_plan")  // 显式指定列名，保持命名一致性
    private String floorPlan;  // 总平面图URL

    @Column(name = "three_d_model")  // 显式指定列名，保持命名一致性
    private String threeDModel;  // 三维模型URL

    @OneToMany(mappedBy = "campus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Building> buildings;  // 关联的楼栋列表
}
