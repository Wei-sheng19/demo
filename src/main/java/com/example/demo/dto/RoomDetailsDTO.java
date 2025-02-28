package com.example.demo.dto;

import java.util.Date;

public record RoomDetailsDTO(
    Integer id,
    String designedPurpose,
    String actualPurpose,
    String functionDescription,
    Double area,
    Double load,
    String electricalSupport,
    Integer constructionYear,
    Date renovationTime,
    String managementDepartment

) {
    public RoomDetailsDTO {
        if (area == null || area <= 0) {
            throw new IllegalArgumentException("Area must be positive");
        }

    }
} 