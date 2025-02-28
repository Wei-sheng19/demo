package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "floor_power_room")
public class FloorPowerRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "average_reference")
    private Double averageReference;  // 平均参考值(KW)

    @Column(name = "min_load_range")
    private Double minLoadRange;  // 可研用电负荷可承载范围最小值(KW)

    @Column(name = "max_load_range")
    private Double maxLoadRange;  // 可研用电负荷可承载范围最大值(KW)

    @Column(name = "design_load")
    private Double designLoad;  // 楼层用电负荷设计值(KW)

    @Column(name = "supportable_load")
    private Double supportableLoad;  // 可支持用电负荷

    @Column(name = "actual_operation_data")
    private Double actualOperationData;  // 现场实际运行数据

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private Floor floor;  // 所属楼层

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "substation_id")
    private Substation substation;  // 所属变电所
}
