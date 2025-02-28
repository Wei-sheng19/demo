package com.example.demo.dto;

public record CampusDTO(
    Long campusId,
    String campusName,
    String location,
    String campusMap,
    String threeDimensionalModel
) {
    // 紧凑型规范构造函数用于验证
    public CampusDTO {
        if (campusName == null || campusName.isBlank()) {
            throw new IllegalArgumentException("Campus name cannot be null or blank");
        }
        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("Location cannot be null or blank");
        }
    }
} 