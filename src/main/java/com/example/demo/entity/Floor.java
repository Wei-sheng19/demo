package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "floor")

public class Floor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="floor_id")
    private Long id;

    @Column(name = "floor_number")  // 确保字段名和数据库列名一致
    private String floorNumber;     // 字段名必须与 repository 方法匹配
    
    @Column(name = "floor_plan")
    private String plan;  // 楼层平面图URL
    
    @Column(name = "height")
    private Double height;  // 层高
    
    @Column(name = "load_capacity")
    private Double load;  // 荷载
    
    @Column(name = "mechanical_info")
    private String mechanicalInfo;  // 机电支持条件
    
    @Column(name = "construction_date")
    private LocalDate constructionDate;  // 建设时间
    
    @Column(name = "renovation_date")
    private LocalDate renovationDate;  // 改造时间


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;  // 所属楼栋

    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Room> rooms;  // 关联的房间列表

    @OneToOne(mappedBy = "floor", cascade = CascadeType.ALL)
    private FloorPowerRoom floorPowerRoom;  // 楼层配电间
} 