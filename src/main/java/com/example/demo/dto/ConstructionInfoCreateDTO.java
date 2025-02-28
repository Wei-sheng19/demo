package com.example.demo.dto;

public record ConstructionInfoCreateDTO(
    Long roomId,
    String projectName,
    String basicConstructionInfo,
    String archivalInfo,
    String maintenanceStandardStatus,
    String buildingBasicInfo
) {
    public ConstructionInfoCreateDTO {
        if (projectName == null || projectName.isBlank()) {
            throw new IllegalArgumentException("Project name cannot be null or blank");
        }
        if (basicConstructionInfo == null || basicConstructionInfo.isBlank()) {
            throw new IllegalArgumentException("Basic construction info cannot be null or blank");
        }
    }
} 