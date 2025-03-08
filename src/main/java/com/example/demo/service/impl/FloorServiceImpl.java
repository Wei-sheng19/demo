package com.example.demo.service.impl;

import com.example.demo.dao.*;
import com.example.demo.dto.building.FloorDTO;
import com.example.demo.dto.building.RoomDTO;
import com.example.demo.dto.equipment.FloorPowerRoomDTO;
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
        return FloorDTO.fromFloor(floor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDTO> getFloorRooms(Long floorId) {
        Floor floor = floorRepository.findById(floorId)
            .orElseThrow(() -> new IllegalArgumentException("Floor not found: " + floorId));
        return floor.getRooms().stream()
            .map(RoomDTO::fromRoom)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FloorPowerRoomDTO getFloorPowerRoom(Long floorId) {
        FloorPowerRoom powerRoom = floorPowerRoomRepository.findByFloorId(floorId);
        if (powerRoom == null) {
            throw new IllegalArgumentException("Power room not found for floor: " + floorId);
        }
        return FloorPowerRoomDTO.fromFloorPowerRoom(powerRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FloorDTO> getBuildingFloors(Long buildingId) {
        List<Floor> floors = floorRepository.findByBuildingId(buildingId);
        return floors.stream()
            .map(FloorDTO::fromFloor)
            .collect(Collectors.toList());
    }
} 