package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
@Table(name = "construction_info")
public class ConstructionInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "construction_info_id")
    private Long constructionInfoId;

    @OneToOne(mappedBy = "constructionInfo", cascade = CascadeType.ALL)
    private Room room;

    @Column(name = "project_name", length = 100)
    private String projectName;

    @Column(name = "basic_construction_info", length = 200)
    private String basicConstructionInfo;

    @Column(name = "archival_info", length = 200)
    private String archivalInfo;

    //运维标准状态
    @Column(name = "maintenance_standard_status", length = 200)
    private String maintenanceStandardStatus;

    @Column(name = "building_basic_info", length = 200)
    private String buildingBasicInfo;

    @Column(name = "audit_info", length = 500)
    private String auditInfo;

    @Column(name = "audit_status")
    @Enumerated(EnumType.STRING)
    private AuditStatus auditStatus;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public enum AuditStatus {
        PENDING,
        APPROVED,
        REJECTED,
        REVIEWING
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
