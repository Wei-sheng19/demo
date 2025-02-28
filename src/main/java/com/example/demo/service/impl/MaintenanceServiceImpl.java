package com.example.demo.service.impl;

import com.example.demo.dao.*;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    @Autowired
    private MaintenanceInfoRepository maintenanceInfoRepository;


    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceRecordDTO> getMaintenanceRecords(Long roomId) {
    Optional<MaintenanceInfo> maintenanceInfos = maintenanceInfoRepository.findByRoomId(roomId);
    return maintenanceInfos.stream()
            .map(maintenanceInfo -> new MaintenanceRecordDTO(
                    maintenanceInfo.getMaintenanceInfoId(),
                   maintenanceInfo.getBasicMaintenanceInfo(),
                    maintenanceInfo.getLaterMaintenance(),
                    maintenanceInfo.getRepairRecords()))
            .collect(Collectors.toList());
}
    @Override
    @Transactional(readOnly = true)
    public List<FeedbackRecordDTO> getFeedbackRecords(Long roomId) {
    Optional<MaintenanceInfo> maintenanceInfos = maintenanceInfoRepository.findByRoomId(roomId);
    return maintenanceInfos.stream()
            .map(maintenanceInfo -> new FeedbackRecordDTO(
                   maintenanceInfo.getMaintenanceInfoId(),
                    maintenanceInfo.getRegularFeedback(),
                    maintenanceInfo.getUserEvaluation(),
                    maintenanceInfo.getServiceRating()))
            .collect(Collectors.toList());
}

    @Override
    @Transactional(readOnly = true)
    public String getAuditInfo(Long maintenanceId) {
        return maintenanceInfoRepository.findAuditInfoById(maintenanceId)
                .orElseThrow(() -> new IllegalArgumentException("Maintenance record not found with id: " + maintenanceId));
    }
} 