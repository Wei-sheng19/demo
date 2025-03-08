package com.example.demo.dto.equipment;

import java.util.Date;
import com.example.demo.entity.MaterialEquipment;

public record MaterialEquipmentDTO(
    Long id,
    String category,
    String materialName,
    String quantityOrArea,
    String technicalRequirements,
    String constructionDepartment,
    String maintenanceDepartment,
    String vendorInfo,
    Double productCost,
    Date installationTime,
    String lifecycleWarningTime,
    Integer replacementPeriod,
    String vendorName,
    String vendorContact,
    String warrantyPeriod,
    String productLifespan,
    String samplePhotos
) {
    // 紧凑型规范构造函数用于验证
    public MaterialEquipmentDTO {
        if (materialName == null || materialName.isBlank()) {
            throw new IllegalArgumentException("Material name cannot be null or blank");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category cannot be null or blank");
        }
    }

    // 静态工厂方法：从MaterialEquipment实体创建MaterialEquipmentDTO
    public static MaterialEquipmentDTO fromMaterialEquipment(MaterialEquipment equipment) {
        return new MaterialEquipmentDTO(
            equipment.getMaterialEquipmentId(),
            equipment.getCategory(),
            equipment.getMaterialName(),
            equipment.getQuantityOrArea(),
            equipment.getTechnicalRequirements(),
            equipment.getConstructionDepartment(),
            equipment.getMaintenanceDepartment(),
            equipment.getVendorInfo(),
            equipment.getProductCost(),
            equipment.getInstallationTime(),
            equipment.getLifecycleWarningTime(),
            equipment.getReplacementPeriod(),
            equipment.getVendorName(),
            equipment.getVendorContact(),
            equipment.getWarrantyPeriod(),
            equipment.getProductLifespan(),
            equipment.getSamplePhotos()
        );
    }
} 