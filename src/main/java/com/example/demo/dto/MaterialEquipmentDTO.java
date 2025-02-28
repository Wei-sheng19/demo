package com.example.demo.dto;

import java.util.Date;

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
} 