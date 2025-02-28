package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "building")  // 显式指定表名
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_id")
    private Long id;

    @Column(name = "building_number", nullable = false)
    private String  buildingNumber;

    @Column(name = "building_name", nullable = false)
    private String name;

    private String purpose;  // 建筑用途

    @Column(name = "construction_date")  // 显式指定列名
    private LocalDate constructionDate;  // 建设年代

    @Column(name = "completion_date")  // 显式指定列名
    private LocalDate completionDate;  // 竣工时间

    private Integer floornumber;  // 层数

    @Column(name = "building_scale")  // 显式指定列名
    private Double buildingScale;  // 建筑规模

    @Column(name = "is_heritage_site")  // 显式指定列名
    private Boolean isHeritageSite;  // 是否文保建筑

    @Column(name = "power_load")
    private Double powerLoad;// 总用电负荷

    // 土地档案相关属性
    @Column(name = "land_number")
    private String landNumber;
    
    @Column(name = "land_area")
    private Double landArea;
    
    @Column(name = "land_usage")
    private String landUsage;

    // 地下空间相关属性
    @Column(name = "basement_floor_count")
    private Integer basementFloorCount;
    
    @Column(name = "basement_area")
    private Double basementArea;
    
    @Column(name = "basement_usage")
    private String basementUsage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id")
    private Campus campus;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Floor> floors;  // 关联的楼层列表
    @ManyToOne
    @JoinColumn(name = "substation_id")
    private Substation substation;
}
