package com.example.demo.dto.building;

import com.example.demo.entity.Building;

public record BuildingBasicDTO(
    Long id,
    String buildingNumber,
    String name,
    String purpose,
    Double powerLoad
) {
    public BuildingBasicDTO {
        if (buildingNumber == null || buildingNumber.isBlank()) {
            throw new IllegalArgumentException("Building number cannot be null or blank");
        }
    }

    // 静态工厂方法：从Building实体创建BuildingBasicDTO
    public static BuildingBasicDTO fromBuilding(Building building) {
        return new BuildingBasicDTO(
            building.getId(),
            building.getBuildingNumber(),
            building.getName(),
            building.getPurpose(),
            building.getPowerLoad()
        );
    }
} 