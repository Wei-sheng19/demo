package com.example.demo.service.impl;

import com.example.demo.dao.*;
import com.example.demo.dto.building.RoomDTO;
import com.example.demo.dto.building.RoomDetailsDTO;
import com.example.demo.dto.building.RoomFunctionDTO;
import com.example.demo.entity.*;
import com.example.demo.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private RoomDetailsRepository roomDetailsRepository;
    
    @Autowired
    private MaintenanceInfoRepository maintenanceInfoRepository;
    
    @Autowired
    private RoomFunctionRepository roomFunctionRepository;

    @Override
    @Transactional(readOnly = true)
    public RoomDTO getRoomBasicInfo(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));
        return new RoomDTO(
                room.getRoomId(),
                room.getRoomNumber(),
                room.getRoomName()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomFunctionDTO> getCurrentFunction(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));
        return room.getRoomFunctions().stream()
                .map(RoomFunctionDTO::fromRoomFunction)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RoomDetailsDTO getRoomDetails(Long roomId) {
        // 查询房间
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));

        // 获取房间详细信息
        RoomDetails roomDetails = room.getRoomDetails();
        if (roomDetails == null) {
            throw new IllegalArgumentException("Room details not found for room id: " + roomId);
        }

        // 将 RoomDetails 映射到 RoomDetailsDTO
        return RoomDetailsDTO.fromRoomDetails(roomDetails);
    }

    @Override
    @Transactional
    public RoomDetailsDTO updateSiteAudit(Long roomId, Map<String, String> auditInfo) {
        // 查询房间
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));

        // 获取房间详细信息
        RoomDetails roomDetails = room.getRoomDetails();
        if (roomDetails == null) {
            throw new IllegalArgumentException("Room details not found for room id: " + roomId);
        }

        // 更新现场审核信息
        if (auditInfo.containsKey("siteAudit")) {
            roomDetails.setSiteAudit(auditInfo.get("siteAudit"));
        }
        if (auditInfo.containsKey("siteAuditpicture")) {
            roomDetails.setSiteAuditpicture(auditInfo.get("siteAuditpicture"));
        }

        // 保存更新
        roomDetails = roomDetailsRepository.save(roomDetails);

        // 返回更新后的DTO
        return RoomDetailsDTO.fromRoomDetails(roomDetails);
    }
} 