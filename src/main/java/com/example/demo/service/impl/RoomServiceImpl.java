package com.example.demo.service.impl;

import com.example.demo.dao.*;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
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
                .map(roomFunction -> new RoomFunctionDTO(
                        roomFunction.getId(),
                        roomFunction.getRoom().getRoomId(),
                        roomFunction.getZoneFunction().getFunctionId(),
                        roomFunction.getZoneFunction().getFunctionName()
                         // 需要根据实际情况填充描述
                ))
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
        return new RoomDetailsDTO(
                roomDetails.getRoomDetailId().intValue(), // 转换为 Integer
                roomDetails.getDesignedPurpose().name(),  // 枚举值转换为字符串
                roomDetails.getActualPurpose().name(),    // 枚举值转换为字符串
                roomDetails.getFunctionDescription(),
                roomDetails.getArea(),
                roomDetails.getLoadCapacity(),
                roomDetails.getElectricalSupport(),
                roomDetails.getConstructionYear(),
                roomDetails.getRenovationTime(),
                roomDetails.getManagementDepartment()
        );
    }
} 