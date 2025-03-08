package com.example.demo.dto.building;

import com.example.demo.entity.Building;

public record LandArchivesDTO(
    String landNumber,
    Double landArea,
    String landUsage
) {
    public LandArchivesDTO {
        if (landNumber == null || landNumber.isBlank()) {
            throw new IllegalArgumentException("Land number cannot be null or blank");
        }
        if (landArea == null || landArea <= 0) {
            throw new IllegalArgumentException("Land area must be positive");
        }
    }

    // 静态工厂方法：从Building实体创建LandArchivesDTO
    public static LandArchivesDTO fromBuilding(Building building) {
        return new LandArchivesDTO(
            building.getLandNumber(),
            building.getLandArea(),
            building.getLandUsage()
        );
    }
} 