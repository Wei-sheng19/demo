package com.example.demo.dto.construction;

import com.example.demo.entity.MaintenanceInfo;

public record FeedbackRecordDTO(
        Long maintenanceInfoId,
        String regularFeedback,
        String userEvaluation,
        Integer serviceRating
) {
    public FeedbackRecordDTO {
        if (regularFeedback == null || regularFeedback.isBlank()) {
            throw new IllegalArgumentException("Regular feedback cannot be null or blank");
        }
    }

    // 静态工厂方法：从MaintenanceInfo实体创建FeedbackRecordDTO
    public static FeedbackRecordDTO fromMaintenanceInfo(MaintenanceInfo info) {
        return new FeedbackRecordDTO(
            info.getMaintenanceInfoId(),
            info.getRegularFeedback(),
            info.getUserEvaluation(),
            info.getServiceRating()
        );
    }
} 