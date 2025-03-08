package com.example.demo.dto.construction;

import com.example.demo.entity.ConstructionInfo.AuditStatus;
import com.example.demo.entity.ConstructionInfo;
import java.util.Date;

public record ConstructionInfoDTO(
    Long id,
    Long roomId,
    String projectName,
    String basicConstructionInfo,
    String archivalInfo,
    String maintenanceStandardStatus,
    String buildingBasicInfo,
    String auditInfo,
    AuditStatus auditStatus,
    Date createdAt,
    Date updatedAt,
    Integer createdBy,
    Integer updatedBy
) {
    // 紧凑型规范构造函数用于验证
    public ConstructionInfoDTO {
        if (projectName == null || projectName.isBlank()) {
            throw new IllegalArgumentException("Project name cannot be null or blank");
        }
        if (basicConstructionInfo == null || basicConstructionInfo.isBlank()) {
            throw new IllegalArgumentException("Basic construction info cannot be null or blank");
        }
    }

    // 静态工厂方法：从ConstructionInfo实体创建ConstructionInfoDTO
    public static ConstructionInfoDTO fromConstructionInfo(ConstructionInfo info) {
        return new ConstructionInfoDTO(
            info.getConstructionInfoId(),
            info.getRoom() != null ? info.getRoom().getRoomId() : null,
            info.getProjectName(),
            info.getBasicConstructionInfo(),
            info.getArchivalInfo(),
            info.getMaintenanceStandardStatus(),
            info.getBuildingBasicInfo(),
            info.getAuditInfo(),
            info.getAuditStatus(),
            info.getCreatedAt(),
            info.getUpdatedAt(),
            info.getCreatedBy(),
            info.getUpdatedBy()
        );
    }
} 