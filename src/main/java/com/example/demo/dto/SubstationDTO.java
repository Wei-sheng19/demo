package com.example.demo.dto;

import java.time.LocalDate;

public record SubstationDTO(
    Long id,
    String name,
    String location,
    LocalDate operationDate,
    Double totalCapacity,
    Double transformerLoadRate,
    String mainComponents,
    String renovationHistory
) {
    public SubstationDTO {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Substation name cannot be null or blank");
        }
        if (totalCapacity == null || totalCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
    }
} 