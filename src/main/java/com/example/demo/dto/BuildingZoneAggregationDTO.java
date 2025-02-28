package com.example.demo.dto;

import java.util.List;
public record BuildingZoneAggregationDTO(
        Integer functionId,
        String functionName,
        Integer roomCount,
        List<RoomBasicDTO> rooms
) {
    // 用于房间基本信息的内部记录
    public record RoomBasicDTO(
        Long roomId,
        String roomNumber,
        String roomName,
        String floorNumber
    ) {}
} 