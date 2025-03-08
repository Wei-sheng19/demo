package com.example.demo.service;

import com.example.demo.dto.building.RoomDTO;
import com.example.demo.dto.building.RoomDetailsDTO;
import com.example.demo.dto.building.RoomFunctionDTO;

import java.util.List;
import java.util.Map;

public interface RoomService {
    RoomDTO getRoomBasicInfo(Long roomId);

    List<RoomFunctionDTO> getCurrentFunction(Long roomId);

    RoomDetailsDTO getRoomDetails(Long roomId);

    RoomDetailsDTO updateSiteAudit(Long roomId, Map<String, String> auditInfo);
}