package com.example.demo.dto.building;

import java.util.List;
import java.util.stream.Collectors;
import com.example.demo.entity.ZoneFunction;
import com.example.demo.entity.Room;

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
    ) {
        // 静态工厂方法：从Room实体创建RoomBasicDTO
        public static RoomBasicDTO fromRoom(Room room) {
            return new RoomBasicDTO(
                room.getRoomId(),
                room.getRoomNumber(),
                room.getRoomName(),
                room.getFloor().getFloorNumber()
            );
        }
    }

    // 静态工厂方法：从ZoneFunction实体和关联的房间列表创建BuildingZoneAggregationDTO
    public static BuildingZoneAggregationDTO fromZoneFunctionAndRooms(
            ZoneFunction zoneFunction,
            List<Room> rooms
    ) {
        List<RoomBasicDTO> roomDTOs = rooms.stream()
            .map(RoomBasicDTO::fromRoom)
            .collect(Collectors.toList());

        return new BuildingZoneAggregationDTO(
            zoneFunction.getFunctionId(),
            zoneFunction.getFunctionName(),
            roomDTOs.size(),
            roomDTOs
        );
    }
} 