package com.example.demo.dto;

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
} 