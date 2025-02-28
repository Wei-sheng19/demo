package com.example.demo.dto;

public record ZoneFunctionDTO(
    Integer zoneFunctionId,
    String zoneType
) {
    public ZoneFunctionDTO {
        if (zoneType == null || zoneType.isBlank()) {
            throw new IllegalArgumentException("Zone type cannot be null or blank");
        }

    }
} 