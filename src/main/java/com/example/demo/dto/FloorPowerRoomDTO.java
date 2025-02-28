package com.example.demo.dto;

public record FloorPowerRoomDTO(
        Long id,
        Double averageReference,
        Double minLoadRange,
        Double maxLoadRange,
        Double designLoad,
        Double supportableLoad,
        Double actualOperationData,
        String floorInfo
) {
    public FloorPowerRoomDTO {
        if (minLoadRange != null && maxLoadRange != null && minLoadRange > maxLoadRange) {
            throw new IllegalArgumentException("Min load range cannot be greater than max load range");
        }
    }
} 