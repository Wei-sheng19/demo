package com.example.demo.dto.equipment;

import java.util.Date;
import com.example.demo.entity.MaterialEquipment;

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
) {
    // 静态工厂方法：从MaterialEquipment实体创建MaterialEquipmentWithLocationDTO
    public static MaterialEquipmentWithLocationDTO fromMaterialEquipment(MaterialEquipment equipment) {
        return new MaterialEquipmentWithLocationDTO(
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
            equipment.getSamplePhotos(),
            equipment.getRoom() != null ? equipment.getRoom().getRoomNumber() : null,
            equipment.getRoom() != null ? equipment.getRoom().getRoomName() : null,
            equipment.getRoom() != null ? equipment.getRoom().getFloor().getFloorNumber() : null
        );
    }
} 