package com.example.demo.service;

import com.example.demo.dto.building.FloorDTO;
import com.example.demo.dto.building.RoomDTO;
import com.example.demo.dto.equipment.FloorPowerRoomDTO;

import java.util.List;

public interface FloorService {
    FloorDTO getFloorInfo(Long floorId);
    List<RoomDTO> getFloorRooms(Long floorId);
    FloorPowerRoomDTO getFloorPowerRoom(Long floorId);
    List<FloorDTO> getBuildingFloors(Long buildingId);
} 