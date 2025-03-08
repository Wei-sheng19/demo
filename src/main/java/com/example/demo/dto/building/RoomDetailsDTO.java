package com.example.demo.dto.building;

import java.util.Date;
import com.example.demo.entity.RoomDetails;

public record RoomDetailsDTO(
    Integer id,
    String designedPurpose,
    String actualPurpose,
    String functionDescription,
    Double area,
    Double load,
    String electricalSupport,
    Integer constructionYear,
    Date renovationTime,
    String managementDepartment,
    String siteAudit,
    String siteAuditpicture
) {
    public RoomDetailsDTO {
        if (area == null || area <= 0) {
            throw new IllegalArgumentException("Area must be positive");
        }
    }

    // 静态工厂方法：从RoomDetails实体创建RoomDetailsDTO
    public static RoomDetailsDTO fromRoomDetails(RoomDetails details) {
        return new RoomDetailsDTO(
            details.getRoomDetailId().intValue(),
            details.getDesignedPurpose().name(),
            details.getActualPurpose().name(),
            details.getFunctionDescription(),
            details.getArea(),
            details.getLoadCapacity(),
            details.getElectricalSupport(),
            details.getConstructionYear(),
            details.getRenovationTime(),
            details.getManagementDepartment(),
            details.getSiteAudit(),
            details.getSiteAuditpicture()
        );
    }
} 