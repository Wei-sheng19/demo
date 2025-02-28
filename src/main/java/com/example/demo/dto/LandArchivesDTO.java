package com.example.demo.dto;

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
} 