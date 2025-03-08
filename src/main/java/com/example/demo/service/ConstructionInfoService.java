package com.example.demo.service;

import com.example.demo.dto.construction.AuditInfoUpdateDTO;
import com.example.demo.dto.construction.ConstructionInfoCreateDTO;
import com.example.demo.dto.construction.ConstructionInfoDTO;
import com.example.demo.dto.construction.ConstructionInfoUpdateDTO;
import com.example.demo.dto.equipment.MaterialEquipmentDTO;
import com.example.demo.dto.equipment.MaterialEquipmentWithLocationDTO;

import java.util.List;
import java.util.Map;

public interface ConstructionInfoService {
    // 基本的CRUD操作
    ConstructionInfoDTO createConstructionInfo(ConstructionInfoCreateDTO createDTO);
    ConstructionInfoDTO updateConstructionInfo(Long id, ConstructionInfoUpdateDTO updateDTO);
    void deleteConstructionInfo(Long id);
    
    // 查询操作
    List<ConstructionInfoDTO> getConstructionInfoByBuildingId(Long buildingId);
    List<ConstructionInfoDTO> getConstructionInfoByFloorId(Long floorId);
    
    // 审核相关操作
    ConstructionInfoDTO updateAuditInfo(Long id, AuditInfoUpdateDTO auditDTO);
    
    // 获取房间建设信息
    ConstructionInfoDTO getRoomConstructionInfo(Long roomId);
    
    // 获取房间的材料设备信息
    List<MaterialEquipmentDTO> getRoomMaterialsAndEquipments(Long roomId);
    
    // 获取楼层特定类型的设备
    List<MaterialEquipmentWithLocationDTO> getFloorSpecificEquipments(Long floorId, String materialName);
    
    // 获取房间特定设备的详细信息
    MaterialEquipmentDTO getRoomEquipmentDetails(Long roomId, Long equipmentId);
    
    // 获取楼层的所有材料设备信息
    List<MaterialEquipmentWithLocationDTO> getFloorMaterialsAndEquipments(Long floorId);
    
    // 获取建筑的所有材料设备信息
    List<MaterialEquipmentWithLocationDTO> getBuildingMaterialsAndEquipments(Long buildingId);

    // 更新运维标准状态
    ConstructionInfoDTO updateMaintenanceStandard(Long roomId, Map<String, String> standardInfo);
} 