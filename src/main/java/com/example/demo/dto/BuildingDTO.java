package com.example.demo.dto;

import java.time.LocalDate;

public record BuildingDTO(
    Long buildingId,
    String buildingNumber,
    String buildingName,
    String buildingPurpose,
    LocalDate constructionDate,
    LocalDate completionDate,
    Integer floorCount,
    Double buildingScale,
    Boolean isHeritageSite,
    Double totalPowerLoad
) {
    // 紧凑型规范构造函数用于验证
    public BuildingDTO {
        if (buildingNumber == null || buildingNumber.isBlank()) {
            throw new IllegalArgumentException("Building number cannot be null or blank");
        }
        if (buildingName == null || buildingName.isBlank()) {
            throw new IllegalArgumentException("Building name cannot be null or blank");
        }
    }
} 