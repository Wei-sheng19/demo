package com.example.demo.dto;

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
} 