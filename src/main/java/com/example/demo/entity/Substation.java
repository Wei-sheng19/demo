package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "substation")
public class Substation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(name = "operation_date")
    private LocalDate operationDate;  // 建设投运时间

    @Column(name = "total_capacity")
    private Double totalCapacity;  // 总装机容量

    @Column(name = "transformer_load_rate")
    private Double transformerLoadRate;  // 变压器负载率

    @Column(name = "main_components")
    private String mainComponents;  // 主要元器件

    @Column(name = "renovation_history")
    private String renovationHistory;  // 改造大修情况

    @OneToMany(mappedBy = "substation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Building> buildings;  // 关联的建筑列表

    @OneToMany(mappedBy = "substation", fetch = FetchType.LAZY)
    private List<FloorPowerRoom> floorPowerRooms;  // 关联的变电间
}
