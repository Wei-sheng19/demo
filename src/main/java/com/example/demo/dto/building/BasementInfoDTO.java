package com.example.demo.dto.building;

import com.example.demo.entity.Building;

public record BasementInfoDTO(
    Integer floorCount,
    Double totalArea,
    String usage
) {
    public BasementInfoDTO {
        if (floorCount == null || floorCount <= 0) {
            throw new IllegalArgumentException("Floor count must be positive");
        }
        if (totalArea == null || totalArea <= 0) {
            throw new IllegalArgumentException("Total area must be positive");
        }
    }

    // 静态工厂方法：从Building实体创建BasementInfoDTO
    public static BasementInfoDTO fromBuilding(Building building) {
        return new BasementInfoDTO(
            building.getBasementFloorCount(),
            building.getBasementArea(),
            building.getBasementUsage()
        );
    }
} 