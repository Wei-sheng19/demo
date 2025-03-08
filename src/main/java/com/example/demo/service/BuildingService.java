package com.example.demo.service;

import com.example.demo.dto.building.BuildingDTO;
import com.example.demo.dto.building.LandArchivesDTO;
import com.example.demo.dto.building.BasementInfoDTO;
import com.example.demo.dto.building.ZoneFunctionDTO;
import com.example.demo.dto.building.FloorDTO;
import com.example.demo.dto.building.BuildingZoneAggregationDTO;
import java.util.List;

public interface BuildingService {
    // 获取楼栋基本信息
    BuildingDTO getBuildingBasicInfo(Long buildingId);
    
    // 获取土地档案信息
    LandArchivesDTO getLandArchives(Long buildingId);
    
    // 获取地下空间信息
    BasementInfoDTO getBasementInfo(Long buildingId);
    

    // 获取楼栋所有楼层
    List<FloorDTO> getBuildingFloors(Long buildingId);
    
    // 获取楼栋分区聚合房间
    BuildingZoneAggregationDTO getBuildingZoneAggregation(Long buildingId, Integer functionId);
    
    // 获取所有可用的分区功能
    List<ZoneFunctionDTO> getAvailableZoneFunctions();
} 