package com.example.demo.service.impl;

import com.example.demo.dao.SubstationRepository;
import com.example.demo.dto.BuildingBasicDTO;
import com.example.demo.dto.FloorPowerRoomDTO;
import com.example.demo.dto.SubstationDTO;
import com.example.demo.entity.FloorPowerRoom;
import com.example.demo.service.SubstationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // 添加 @Service 注解
import java.util.List;
import com.example.demo.entity.Substation;

@Service // 确保使用 @Service 注解
public class SubstationServiceImpl implements SubstationService {
    @Autowired
    private SubstationRepository substationRepository;

    @Override
    public SubstationDTO getSubstationInfo(Long substationId) {
        Substation substation = substationRepository.findById(substationId)
            .orElseThrow(() -> new IllegalArgumentException("Substation not found with id: " + substationId));
        
        return new SubstationDTO(
            substation.getId(),
            substation.getName(),
            substation.getLocation(),
            substation.getOperationDate(),
            substation.getTotalCapacity(),
            substation.getTransformerLoadRate(),
            substation.getMainComponents(),
            substation.getRenovationHistory()
        );
    }

    @Override
    public List<BuildingBasicDTO> getSupplyBuildings(Long substationId) {
        Substation substation = substationRepository.findWithBuildings(substationId);
        if (substation == null) {
            throw new IllegalArgumentException("Substation not found with id: " + substationId);
        }
        
        return substation.getBuildings().stream()
            .map(b -> new BuildingBasicDTO(
                b.getId(),
                b.getBuildingNumber(),
                b.getName(),
                b.getPurpose(),
                b.getPowerLoad()
            ))
            .toList();
    }

    @Override
    public List<FloorPowerRoomDTO> getSubPowerRooms(Long substationId) {
        Substation substation = substationRepository.findById(substationId)
                .orElseThrow(() -> new IllegalArgumentException("Substation not found with id: " + substationId));

        // 分步加载 floorPowerRooms
        List<FloorPowerRoom> floorPowerRooms = substation.getFloorPowerRooms();

        return floorPowerRooms.stream()
                .map(powerRoom -> new FloorPowerRoomDTO(
                        powerRoom.getId(),
                        powerRoom.getAverageReference(),
                        powerRoom.getMinLoadRange(),
                        powerRoom.getMaxLoadRange(),
                        powerRoom.getDesignLoad(),
                        powerRoom.getSupportableLoad(),
                        powerRoom.getActualOperationData(),
                        powerRoom.getFloor() != null ? "Floor " + powerRoom.getFloor().getFloorNumber() : "N/A"))
                .toList();
    }

}