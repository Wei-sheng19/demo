package com.example.demo.dto.construction;

public record ConstructionInfoUpdateDTO(
    String projectName,
    String basicConstructionInfo,
    String archivalInfo,
    String maintenanceStandardStatus,
    String buildingBasicInfo
) {} 