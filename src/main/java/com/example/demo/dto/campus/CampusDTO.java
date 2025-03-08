package com.example.demo.dto.campus;

import com.example.demo.entity.Campus;

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

    // 静态工厂方法：从Campus实体创建CampusDTO
    public static CampusDTO fromCampus(Campus campus) {
        return new CampusDTO(
            campus.getId(),
            campus.getName(),
            campus.getLocation(),
            campus.getFloorPlan(),
            campus.getThreeDModel()
        );
    }
} 