package com.example.demo.service;

import com.example.demo.dto.equipment.FloorPowerRoomDTO;
import com.example.demo.dto.equipment.SubstationDTO;
import com.example.demo.dto.building.BuildingBasicDTO;
import java.util.List;

public interface SubstationService {
    SubstationDTO getSubstationInfo(Long substationId);
    List<BuildingBasicDTO> getSupplyBuildings(Long substationId);
    List<FloorPowerRoomDTO> getSubPowerRooms(Long substationId);
} 