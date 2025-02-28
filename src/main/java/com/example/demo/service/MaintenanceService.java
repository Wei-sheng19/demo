package com.example.demo.service;

import com.example.demo.dto.*;
import java.util.List;

public interface MaintenanceService {
    List<MaintenanceRecordDTO> getMaintenanceRecords(Long roomId);
    List<FeedbackRecordDTO> getFeedbackRecords(Long roomId);
    String getAuditInfo(Long maintenanceId);
} 