package com.example.demo.dto.building;

import com.example.demo.entity.ZoneFunction;

public record ZoneFunctionDTO(
    Integer zoneFunctionId,
    String zoneType
) {
    public ZoneFunctionDTO {
        if (zoneType == null || zoneType.isBlank()) {
            throw new IllegalArgumentException("Zone type cannot be null or blank");
        }
    }

    // 静态工厂方法：从ZoneFunction实体创建ZoneFunctionDTO
    public static ZoneFunctionDTO fromZoneFunction(ZoneFunction zoneFunction) {
        return new ZoneFunctionDTO(
            zoneFunction.getFunctionId(),
            zoneFunction.getFunctionName()
        );
    }
} 