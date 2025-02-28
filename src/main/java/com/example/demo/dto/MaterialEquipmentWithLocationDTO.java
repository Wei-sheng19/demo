package com.example.demo.dto;

import java.util.Date;

public record MaterialEquipmentWithLocationDTO(
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
    String samplePhotos,
    String roomNumber,    // 添加房间号
    String roomName,      // 添加房间名称
    String floorNumber    // 添加楼层号
) {} 