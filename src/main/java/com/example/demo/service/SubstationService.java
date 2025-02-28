package com.example.demo.service;

import com.example.demo.dto.FloorPowerRoomDTO;
import com.example.demo.dto.SubstationDTO;
import com.example.demo.dto.BuildingBasicDTO;
import java.util.List;

public interface SubstationService {
    SubstationDTO getSubstationInfo(Long substationId);
    List<BuildingBasicDTO> getSupplyBuildings(Long substationId);
    List<FloorPowerRoomDTO> getSubPowerRooms(Long substationId);
} 