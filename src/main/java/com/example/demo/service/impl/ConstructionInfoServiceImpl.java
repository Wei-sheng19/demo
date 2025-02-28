package com.example.demo.service.impl;

import com.example.demo.dao.ConstructionInfoRepository;
import com.example.demo.dao.MaterialEquipmentRepository;
import com.example.demo.dao.RoomRepository;
import com.example.demo.dto.*;
import com.example.demo.entity.ConstructionInfo;
import com.example.demo.entity.MaterialEquipment;
import com.example.demo.entity.Room;
import com.example.demo.service.ConstructionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConstructionInfoServiceImpl implements ConstructionInfoService {

    @Autowired
    private ConstructionInfoRepository constructionInfoRepository;
    
    @Autowired
    private MaterialEquipmentRepository materialEquipmentRepository;
    
    @Autowired
    private RoomRepository roomRepository;

    @Override
    @Transactional
    public ConstructionInfoDTO createConstructionInfo(ConstructionInfoCreateDTO createDTO) {
        Room room = roomRepository.findById(createDTO.roomId())
            .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + createDTO.roomId()));
            
        ConstructionInfo constructionInfo = new ConstructionInfo();
        constructionInfo.setRoom(room);
        constructionInfo.setProjectName(createDTO.projectName());
        constructionInfo.setBasicConstructionInfo(createDTO.basicConstructionInfo());
        constructionInfo.setArchivalInfo(createDTO.archivalInfo());
        constructionInfo.setMaintenanceStandardStatus(createDTO.maintenanceStandardStatus());
        constructionInfo.setBuildingBasicInfo(createDTO.buildingBasicInfo());
        constructionInfo.setCreatedAt(new Date());
        constructionInfo.setAuditStatus(ConstructionInfo.AuditStatus.PENDING);
        
        ConstructionInfo saved = constructionInfoRepository.save(constructionInfo);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public ConstructionInfoDTO updateConstructionInfo(Long id, ConstructionInfoUpdateDTO updateDTO) {
        ConstructionInfo constructionInfo = constructionInfoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Construction info not found with id: " + id));
            
        if (updateDTO.projectName() != null) {
            constructionInfo.setProjectName(updateDTO.projectName());
        }
        if (updateDTO.basicConstructionInfo() != null) {
            constructionInfo.setBasicConstructionInfo(updateDTO.basicConstructionInfo());
        }
        if (updateDTO.archivalInfo() != null) {
            constructionInfo.setArchivalInfo(updateDTO.archivalInfo());
        }
        if (updateDTO.maintenanceStandardStatus() != null) {
            constructionInfo.setMaintenanceStandardStatus(updateDTO.maintenanceStandardStatus());
        }
        if (updateDTO.buildingBasicInfo() != null) {
            constructionInfo.setBuildingBasicInfo(updateDTO.buildingBasicInfo());
        }
        
        ConstructionInfo saved = constructionInfoRepository.save(constructionInfo);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public void deleteConstructionInfo(Long id) {
        ConstructionInfo constructionInfo = constructionInfoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Construction info not found with id: " + id));
            
        constructionInfo.setIsDeleted(true);
        constructionInfoRepository.save(constructionInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConstructionInfoDTO> getConstructionInfoByBuildingId(Long buildingId) {
        return constructionInfoRepository.findByBuildingId(buildingId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConstructionInfoDTO> getConstructionInfoByFloorId(Long floorId) {
        return constructionInfoRepository.findByFloorId(floorId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ConstructionInfoDTO updateAuditInfo(Long id, AuditInfoUpdateDTO auditDTO) {
        ConstructionInfo constructionInfo = constructionInfoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Construction info not found with id: " + id));
            
        constructionInfo.setAuditInfo(auditDTO.auditInfo());
        constructionInfo.setAuditStatus(auditDTO.auditStatus());
        constructionInfo.setUpdatedAt(new Date());
        
        ConstructionInfo saved = constructionInfoRepository.save(constructionInfo);
        return convertToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ConstructionInfoDTO getRoomConstructionInfo(Long roomId) {
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));
            
        ConstructionInfo constructionInfo = room.getConstructionInfo();
        if (constructionInfo == null) {
            throw new IllegalArgumentException("Construction info not found for room: " + roomId);
        }
        
        return convertToDTO(constructionInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialEquipmentDTO> getRoomMaterialsAndEquipments(Long roomId) {
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));
            
        return room.getMaterialEquipments().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialEquipmentWithLocationDTO> getFloorSpecificEquipments(Long floorId, String materialName) {
        return materialEquipmentRepository.findDetailsByFloorIdAndMaterialName(floorId, materialName);
    }

    @Override
    @Transactional(readOnly = true)
    public MaterialEquipmentDTO getRoomEquipmentDetails(Long roomId, Long equipmentId) {
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));
            
        return room.getMaterialEquipments().stream()
            .filter(eq -> eq.getMaterialEquipmentId().equals(equipmentId))
            .findFirst()
            .map(this::convertToDTO)
            .orElseThrow(() -> new IllegalArgumentException(
                "Equipment not found with id: " + equipmentId + " in room: " + roomId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialEquipmentWithLocationDTO> getFloorMaterialsAndEquipments(Long floorId) {
        return materialEquipmentRepository.findDistinctDetailsByFloorId(floorId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialEquipmentWithLocationDTO> getBuildingMaterialsAndEquipments(Long buildingId) {
        return materialEquipmentRepository.findDistinctDetailsByBuildingId(buildingId);
    }

    private ConstructionInfoDTO convertToDTO(ConstructionInfo info) {
        return new ConstructionInfoDTO(
            info.getConstructionInfoId(),
            info.getRoom().getRoomId(),
            info.getProjectName(),
            info.getBasicConstructionInfo(),
            info.getArchivalInfo(),
            info.getMaintenanceStandardStatus(),
            info.getBuildingBasicInfo(),
            info.getAuditInfo(),
            info.getAuditStatus(),
            info.getCreatedAt(),
            info.getUpdatedAt(),
            info.getCreatedBy(),
            info.getUpdatedBy()
        );
    }

    private MaterialEquipmentDTO convertToDTO(MaterialEquipment equipment) {
        return new MaterialEquipmentDTO(
            equipment.getMaterialEquipmentId(),
            equipment.getCategory(),
            equipment.getMaterialName(),
            equipment.getQuantityOrArea(),
            equipment.getTechnicalRequirements(),
            equipment.getConstructionDepartment(),
            equipment.getMaintenanceDepartment(),
            equipment.getVendorInfo(),
            equipment.getProductCost(),
            equipment.getInstallationTime(),
            equipment.getLifecycleWarningTime(),
            equipment.getReplacementPeriod(),
            equipment.getVendorName(),
            equipment.getVendorContact(),
            equipment.getWarrantyPeriod(),
            equipment.getProductLifespan(),
            equipment.getSamplePhotos()
        );
    }
} 