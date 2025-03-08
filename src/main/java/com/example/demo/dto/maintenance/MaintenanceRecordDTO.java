package com.example.demo.dto.maintenance;

import com.example.demo.entity.MaintenanceInfo;

public record MaintenanceRecordDTO(
        Long maintenanceInfoId,
        String basicMaintenanceInfo,
        String laterMaintenance,
        String repairRecords
) {
    public MaintenanceRecordDTO {
        if (basicMaintenanceInfo == null || basicMaintenanceInfo.isBlank()) {
            throw new IllegalArgumentException("Basic maintenance info cannot be null or blank");
        }
    }

    // 静态工厂方法：从MaintenanceInfo实体创建MaintenanceRecordDTO
    public static MaintenanceRecordDTO fromMaintenanceInfo(MaintenanceInfo info) {
        return new MaintenanceRecordDTO(
            info.getMaintenanceInfoId(),
            info.getBasicMaintenanceInfo(),
            info.getLaterMaintenance(),
            info.getRepairRecords()
        );
    }
} 