package com.example.demo.dto.building;

import com.example.demo.entity.RoomFunction;

public record RoomFunctionDTO(
        Long id,
        Long roomId,
        Integer zoneFunctionId,
        String functionType
) {
    public RoomFunctionDTO {
        if (functionType == null || functionType.isBlank()) {
            throw new IllegalArgumentException("Function type cannot be null or blank");
        }
    }

    // 静态工厂方法：从RoomFunction实体创建RoomFunctionDTO
    public static RoomFunctionDTO fromRoomFunction(RoomFunction roomFunction) {
        return new RoomFunctionDTO(
                roomFunction.getId(),
                roomFunction.getRoom().getRoomId(),
                roomFunction.getZoneFunction().getFunctionId(),
                roomFunction.getZoneFunction().getFunctionName()
        );
    }
} 