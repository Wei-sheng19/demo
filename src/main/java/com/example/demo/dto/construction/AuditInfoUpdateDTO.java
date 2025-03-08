package com.example.demo.dto.construction;

import com.example.demo.entity.ConstructionInfo.AuditStatus;

public record AuditInfoUpdateDTO(
    String auditInfo,
    AuditStatus auditStatus
) {
    public AuditInfoUpdateDTO {
        if (auditInfo == null || auditInfo.isBlank()) {
            throw new IllegalArgumentException("Audit info cannot be null or blank");
        }
        if (auditStatus == null) {
            throw new IllegalArgumentException("Audit status cannot be null");
        }
    }
} 