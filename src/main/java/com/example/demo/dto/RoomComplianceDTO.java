package com.example.demo.dto;

public record RoomComplianceDTO(
    String complianceStatus,
    String complianceNotes
) {
    public RoomComplianceDTO {
        if (complianceStatus == null || complianceStatus.isBlank()) {
            throw new IllegalArgumentException("Compliance status cannot be null or blank");
        }
    }
} 