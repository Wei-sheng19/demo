package com.example.demo.dto.building;

import com.example.demo.entity.Room;

public record RoomDTO(
    Long roomId,
    String roomNumber,
    String roomName
) {
    public RoomDTO {
        if (roomNumber == null || roomNumber.isBlank()) {
            throw new IllegalArgumentException("Room number cannot be null or blank");
        }
    }

    // 静态工厂方法：从Room实体创建RoomDTO
    public static RoomDTO fromRoom(Room room) {
        return new RoomDTO(
            room.getRoomId(),
            room.getRoomNumber(),
            room.getRoomName()
        );
    }
} 