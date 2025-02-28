package com.example.demo.dto;

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
} 