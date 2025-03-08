package com.example.demo.dto.equipment;

import java.time.LocalDate;
import com.example.demo.entity.Substation;

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

    // 静态工厂方法：从Substation实体创建SubstationDTO
    public static SubstationDTO fromSubstation(Substation substation) {
        return new SubstationDTO(
            substation.getId(),
            substation.getName(),
            substation.getLocation(),
            substation.getOperationDate(),
            substation.getTotalCapacity(),
            substation.getTransformerLoadRate(),
            substation.getMainComponents(),
            substation.getRenovationHistory()
        );
    }
} 