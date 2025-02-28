package com.example.demo.dto;

import java.time.LocalDate;

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
} 