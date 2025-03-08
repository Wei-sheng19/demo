package com.example.demo.dto.equipment;

import com.example.demo.entity.FloorPowerRoom;

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

    // 静态工厂方法：从FloorPowerRoom实体创建FloorPowerRoomDTO
    public static FloorPowerRoomDTO fromFloorPowerRoom(FloorPowerRoom powerRoom) {
        return new FloorPowerRoomDTO(
            powerRoom.getId(),
            powerRoom.getAverageReference(),
            powerRoom.getMinLoadRange(),
            powerRoom.getMaxLoadRange(),
            powerRoom.getDesignLoad(),
            powerRoom.getSupportableLoad(),
            powerRoom.getActualOperationData(), powerRoom.getFloor() != null ? "Floor " + powerRoom.getFloor().getFloorNumber() : "N/A"
        );
    }
} 