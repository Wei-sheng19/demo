package com.example.demo.service.impl;

import com.example.demo.dao.*;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.service.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FloorServiceImpl implements FloorService {

    @Autowired
    private FloorRepository floorRepository;
    
    @Autowired
    private FloorPowerRoomRepository floorPowerRoomRepository;

    @Override
    @Transactional(readOnly = true)
    public FloorDTO getFloorInfo(Long floorId) {
        Floor floor = floorRepository.findById(floorId)
            .orElseThrow(() -> new IllegalArgumentException("Floor not found: " + floorId));
        return new FloorDTO(
                floor.getId(),
                floor.getFloorNumber(),
                floor.getPlan(),
                floor.getHeight(),
                floor.getLoad(),
                floor.getMechanicalInfo(),
                floor.getConstructionDate(),
                floor.getRenovationDate());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDTO> getFloorRooms(Long floorId) {
        Floor floor = floorRepository.findById(floorId)
            .orElseThrow(() -> new IllegalArgumentException("Floor not found: " + floorId));
        return floor.getRooms().stream()
            .map(room -> new RoomDTO(
                    room.getRoomId(),
                    room.getRoomNumber(),
                    room.getRoomName()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FloorPowerRoomDTO getFloorPowerRoom(Long floorId) {
        FloorPowerRoom powerRoom = floorPowerRoomRepository.findByFloorId(floorId);
        if (powerRoom == null) {
            throw new IllegalArgumentException("Power room not found for floor: " + floorId);
        }
        return new FloorPowerRoomDTO(
                powerRoom.getId(),
                powerRoom.getAverageReference(),
                powerRoom.getMinLoadRange(),
                powerRoom.getMaxLoadRange(),
                powerRoom.getDesignLoad(),
                powerRoom.getSupportableLoad(),
                powerRoom.getActualOperationData(),
                powerRoom.getFloor() != null ? "Floor " + powerRoom.getFloor().getFloorNumber() : "N/A");
    }

    @Override
    @Transactional(readOnly = true)
    public List<FloorDTO> getBuildingFloors(Long buildingId) {
        List<Floor> floors = floorRepository.findByBuildingId(buildingId);
        return floors.stream()
            .map(floor -> new FloorDTO(
                    floor.getId(),
                    floor.getFloorNumber(),
                    floor.getPlan(),
                    floor.getHeight(),
                    floor.getLoad(),
                    floor.getMechanicalInfo(),
                    floor.getConstructionDate(),
                    floor.getRenovationDate()))
            .collect(Collectors.toList());
    }
} 