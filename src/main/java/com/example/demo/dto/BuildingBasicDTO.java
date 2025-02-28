package com.example.demo.dto;

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
} 