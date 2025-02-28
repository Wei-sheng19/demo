package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
@Table(name = "room_details")
public class RoomDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_detail_id")
    private Long roomDetailId;  // 房间详细信息ID

    @OneToOne
    @JoinColumn(name = "room_id", nullable = false, unique = true)
    private Room room;  // 外键，关联房间表

    @Enumerated(EnumType.STRING)
    @Column(name = "designed_purpose", nullable = false)
    private Purpose designedPurpose;  // 房间设计用途

    @Enumerated(EnumType.STRING)
    @Column(name = "actual_purpose", nullable = false)
    private Purpose actualPurpose;  // 房间实际用途

    @Column(name = "function_description", length = 200)
    private String functionDescription;  // 功能描述

    @Column(name = "area", precision = 10)
    private Double area;  // 面积

    @Column(name = "load_capacity", precision = 10)
    private Double loadCapacity;  // 荷载

    @Column(name = "electrical_support", length = 100)
    private String electricalSupport;  // 机电支持条件

    @Column(name = "construction_year")
    private Integer constructionYear;  // 建设时间

    @Column(name = "renovation_time")
    private Date renovationTime;  // 改造时间

    @Column(name = "management_department", length = 100)
    private String managementDepartment;  // 管理归属部门

    // 枚举类定义房间用途
    public enum Purpose {
        CLASSROOM,  // 教室
        LABORATORY,  // 实验室
        MEETING_ROOM,  // 会议室
        OFFICE,  // 办公室
        OTHER  // 其他
    }
}
