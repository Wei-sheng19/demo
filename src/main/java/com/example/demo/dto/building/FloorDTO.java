package com.example.demo.dto.building;

import java.time.LocalDate;
import com.example.demo.entity.Floor;

public record FloorDTO(
    Long id,
    String floorNumber,
    String floorPlan,
    Double height,
    Double load,
    String mechanicalSupport,
    LocalDate constructionDate,
    LocalDate renovationDate
) {
    public FloorDTO {
        if (floorNumber == null || floorNumber.isBlank()) {
            throw new IllegalArgumentException("Floor number cannot be null or blank");
        }
        if (height == null || height <= 0) {
            throw new IllegalArgumentException("Height must be positive");
        }
    }

    // 静态工厂方法：从Floor实体创建FloorDTO
    public static FloorDTO fromFloor(Floor floor) {
        return new FloorDTO(
            floor.getId(),
            floor.getFloorNumber(),
            floor.getPlan(),
            floor.getHeight(),
            floor.getLoad(),
            floor.getMechanicalInfo(),
            floor.getConstructionDate(),
            floor.getRenovationDate()
        );
    }
} 