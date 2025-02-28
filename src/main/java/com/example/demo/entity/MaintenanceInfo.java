package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
@Table(name = "maintenance_info")
public class MaintenanceInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maintenance_info_id")
    private Long maintenanceInfoId;  // 修改为Long类型

    @OneToOne(mappedBy = "maintenanceInfo", cascade = CascadeType.ALL)
    private Room room;  // 外键，关联房间表

    @Column(name = "basic_maintenance_info", length = 200)
    private String basicMaintenanceInfo;  // 基本运维信息

    @Column(name = "later_maintenance", length = 200)
    private String laterMaintenance;  // 后期运维

    @Column(name = "regular_feedback", length = 200)
    private String regularFeedback;  // 定期反馈检查

    @Column(name = "repair_records", length = 200)
    private String repairRecords;  // 维修记录

    @Column(name = "problem_judgment_and_guidance", length = 200)
    private String problemJudgmentAndGuidance;  // 问题判断与指引

    @Column(name = "user_evaluation", length = 200)
    private String userEvaluation;  // 使用方评价

    @Column(name = "service_rating")
    private Integer serviceRating;  // 优质服务等级（1-5星）

    @Column(name = "audit_info")
    private String auditInfo;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    private Date createdAt;  // 创建时间
}
