package com.example.demo.dto;

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
} 