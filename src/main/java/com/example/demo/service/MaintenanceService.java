package com.example.demo.service;

import com.example.demo.dto.construction.FeedbackRecordDTO;
import com.example.demo.dto.maintenance.MaintenanceRecordDTO;

import java.util.List;

public interface MaintenanceService {
    List<MaintenanceRecordDTO> getMaintenanceRecords(Long roomId);
    List<FeedbackRecordDTO> getFeedbackRecords(Long roomId);
    String getStandardStatus(Long maintenanceId);
} 