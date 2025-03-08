package com.example.demo.dto.building;

import java.time.LocalDate;
import com.example.demo.entity.Building;

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

    // 静态工厂方法：从Building实体创建BuildingDTO
    public static BuildingDTO fromBuilding(Building building) {
        return new BuildingDTO(
            building.getId(),
            building.getBuildingNumber(),
            building.getName(),
            building.getPurpose(),
            building.getConstructionDate(),
            building.getCompletionDate(),
            building.getFloornumber(),
            building.getBuildingScale(),
            building.getIsHeritageSite(),
            building.getPowerLoad()
        );
    }
} 