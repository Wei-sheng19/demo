package com.example.demo.validator.impl;

import com.example.demo.validator.ForeignKeyValidator;
import com.example.demo.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DefaultForeignKeyValidator implements ForeignKeyValidator {
    
    @Autowired
    private CampusRepository campusRepository;
    
    @Autowired
    private BuildingRepository buildingRepository;
    
    @Autowired
    private FloorRepository floorRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private SubstationRepository substationRepository;
    
    @Autowired
    private ZoneFunctionRepository zoneFunctionRepository;
    
    @Override
    public List<String> validateForeignKeys(String entityType, Map<String, String> data) {
        List<String> errors = new ArrayList<>();
        
        switch (entityType.toLowerCase()) {
            case "building":
                validateCampusId(data.get("campus_id"), errors);
                validateOptionalSubstationId(data.get("substation_id"), errors);
                break;
                
            case "floor":
                validateBuildingId(data.get("building_id"), errors);
                break;
                
            case "room":
                validateFloorId(data.get("floor_id"), errors);
                break;
                
            case "floorpowerroom":
                validateFloorId(data.get("floor_id"), errors);
                validateSubstationId(data.get("substation_id"), errors);
                break;
                
            case "roomfunction":
                validateRoomId(data.get("room_id"), errors);
                validateZoneFunctionId(data.get("zone_function_id"), errors);
                break;
                
            case "roomdetails":
                validateRoomIdUnique(data.get("room_id"), errors);
                break;
                
            case "materialequipment":
            case "constructioninfo":
            case "maintenanceinfo":
                validateRoomId(data.get("room_id"), errors);
                break;
        }
        
        return errors;
    }
    
    private void validateCampusId(String campusId, List<String> errors) {
        if (!StringUtils.hasText(campusId)) {
            errors.add("campus_id is required");
            return;
        }
        if (!campusRepository.existsById(Long.valueOf(campusId))) {
            errors.add("Referenced campus_id does not exist: " + campusId);
        }
    }
    
    private void validateBuildingId(String buildingId, List<String> errors) {
        if (!StringUtils.hasText(buildingId)) {
            errors.add("building_id is required");
            return;
        }
        if (!buildingRepository.existsById(Long.valueOf(buildingId))) {
            errors.add("Referenced building_id does not exist: " + buildingId);
        }
    }
    
    private void validateFloorId(String floorId, List<String> errors) {
        if (!StringUtils.hasText(floorId)) {
            errors.add("floor_id is required");
            return;
        }
        if (!floorRepository.existsById(Long.valueOf(floorId))) {
            errors.add("Referenced floor_id does not exist: " + floorId);
        }
    }
    
    private void validateSubstationId(String substationId, List<String> errors) {
        if (!StringUtils.hasText(substationId)) {
            errors.add("substation_id is required");
            return;
        }
        if (!substationRepository.existsById(Long.valueOf(substationId))) {
            errors.add("Referenced substation_id does not exist: " + substationId);
        }
    }
    
    private void validateOptionalSubstationId(String substationId, List<String> errors) {
        if (StringUtils.hasText(substationId) && 
            !substationRepository.existsById(Long.valueOf(substationId))) {
            errors.add("Referenced substation_id does not exist: " + substationId);
        }
    }
    
    private void validateRoomId(String roomId, List<String> errors) {
        if (!StringUtils.hasText(roomId)) {
            errors.add("room_id is required");
            return;
        }
        if (!roomRepository.existsById(Long.valueOf(roomId))) {
            errors.add("Referenced room_id does not exist: " + roomId);
        }
    }
    
    private void validateRoomIdUnique(String roomId, List<String> errors) {
        if (!StringUtils.hasText(roomId)) {
            errors.add("room_id is required");
            return;
        }
        if (!roomRepository.existsById(Long.valueOf(roomId))) {
            errors.add("Referenced room_id does not exist: " + roomId);
            return;
        }
        // 检查是否已存在该房间的详情记录
        if (roomRepository.existsByRoomDetailsRoomId(Long.valueOf(roomId))) {
            errors.add("Room details already exist for room_id: " + roomId);
        }
    }
    
    private void validateZoneFunctionId(String zoneFunctionId, List<String> errors) {
        if (!StringUtils.hasText(zoneFunctionId)) {
            errors.add("zone_function_id is required");
            return;
        }
        if (!zoneFunctionRepository.existsById(Integer.valueOf(zoneFunctionId))) {
            errors.add("Referenced zone_function_id does not exist: " + zoneFunctionId);
        }
    }
} 