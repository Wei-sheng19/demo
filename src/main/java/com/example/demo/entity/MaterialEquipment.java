package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
@Table(name = "material_equipment")
public class MaterialEquipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_equipment_id")
    private Long materialEquipmentId;  // 材料设备ID

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;  // 外键，关联房间表

    @Column(name = "category", length = 100)
    private String category;  // 类别

    @Column(name = "material_name", length = 100)
    private String materialName;  // 材料（设备）名称

    @Column(name = "quantity_or_area", length = 50)
    private String quantityOrArea;  // 个数/面积

    @Column(name = "technical_requirements", length = 200)
    private String technicalRequirements;  // 技术要求

    @Column(name = "construction_department", length = 100)
    private String constructionDepartment;  // 建设责任部门

    @Column(name = "maintenance_department", length = 100)
    private String maintenanceDepartment;  // 运维责任部门

    @Column(name = "vendor_info", length = 200)
    private String vendorInfo;  // 服务商供货信息

    @Column(name = "product_cost", precision = 10)
    private Double productCost;  // 产品造价（定额）

    @Column(name = "installation_time")
    private Date installationTime;  // 安装时间

    @Column(name = "lifecycle_warning_time", length = 50)
    private String lifecycleWarningTime;  // 使用寿命预警更换时间

    @Column(name = "replacement_period")
    private Integer replacementPeriod;  // 规定更换年限

    @Column(name = "vendor_name", length = 100)
    private String vendorName;  // 服务商名称

    @Column(name = "vendor_contact", length = 100)
    private String vendorContact;  // 服务商联系方式

    @Column(name = "warranty_period", length = 50)
    private String warrantyPeriod;  // 质保时间

    @Column(name = "product_lifespan", length = 50)
    private String productLifespan;  // 商品使用寿命

    @Column(name = "sample_photos", length = 200)
    private String samplePhotos;  // 样板照片

}
