package com.example.demo.dto;

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
} 