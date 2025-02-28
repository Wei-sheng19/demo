package com.example.demo.dto;

public record ConstructionInfoUpdateDTO(
    String projectName,
    String basicConstructionInfo,
    String archivalInfo,
    String maintenanceStandardStatus,
    String buildingBasicInfo
) {} 