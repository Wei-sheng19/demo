package com.example.demo.service.impl;

import com.example.demo.dao.BuildingRepository;
import com.example.demo.dao.ZoneFunctionRepository;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;
    

    @Autowired
    private ZoneFunctionRepository zoneFunctionRepository;

    /**
     * 重写获取建筑物基本信息的方法
     * 该方法仅用于读取操作，因此声明为只读事务
     *
     * @param buildingId 建筑物ID，用于查询建筑物信息
     * @return BuildingDTO 返回建筑物的基本信息DTO对象
     * @throws RuntimeException 如果未找到指定ID的建筑物，则抛出运行时异常
     */
    @Override
    @Transactional(readOnly = true)
    public BuildingDTO getBuildingBasicInfo(Long buildingId) {
        // 尝试通过建筑物ID获取建筑物实体
        Optional<Building> buildingOptional = buildingRepository.findById(buildingId);
        if (buildingOptional.isPresent()) {
            // 如果找到建筑物，获取建筑物实体并创建并返回建筑物DTO对象
            Building building = buildingOptional.get();
            return new BuildingDTO(
                    building.getId(),
                    building.getBuildingNumber(),
                    building.getName(),
                    building.getPurpose(),
                    building.getConstructionDate(),
                    building.getCompletionDate(),
                    building.getFloornumber(),
                    building.getBuildingScale(),
                    building.getIsHeritageSite(),
                    building.getPowerLoad()
            );
        } else {
            // 如果未找到建筑物，抛出异常
            throw new RuntimeException("Building not found with ID: " + buildingId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public LandArchivesDTO getLandArchives(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
            .orElseThrow(() -> new IllegalArgumentException("Building not found with id: " + buildingId));
            
        return new LandArchivesDTO(
            building.getLandNumber(),
            building.getLandArea(),
            building.getLandUsage()
        );
    }

    /**
     * 获取建筑物地下室信息
     *
     * @param buildingId 建筑物ID，用于查询地下室信息
     * @return BasementInfoDTO 返回地下室信息DTO对象
     * @throws RuntimeException 如果未找到指定ID的建筑物，则抛出运行时异常
     */
    @Override
    public BasementInfoDTO getBasementInfo(Long buildingId) {
        // 尝试通过建筑物ID获取建筑物实体
        Optional<Building> buildingOptional = buildingRepository.findById(buildingId);
        if (buildingOptional.isPresent()) {
            // 如果找到建筑物，获取建筑物实体并创建并返回地下室信息DTO对象
            Building building = buildingOptional.get();
            return new BasementInfoDTO(
                    building.getBasementFloorCount(),
                    building.getBasementArea(),
                    building.getBasementUsage()
            );
        } else {
            // 如果未找到建筑物，抛出异常
            throw new RuntimeException("Building not found with ID: " + buildingId);
        }
    }

    /**
     * 获取建筑物的所有楼层信息
     *
     * @param buildingId 建筑物ID，用于查询楼层信息
     * @return List<FloorDTO> 返回楼层信息DTO对象列表
     * @throws RuntimeException 如果未找到指定ID的建筑物，则抛出运行时异常
     */
    @Override
    @Transactional(readOnly = true)
    public List<FloorDTO> getBuildingFloors(Long buildingId) {
        // 尝试通过建筑物ID获取建筑物实体
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new RuntimeException("Building not found with ID: " + buildingId));

        // 获取建筑物的所有楼层
        List<Floor> floors = building.getFloors();

        // 将楼层实体转换为FloorDTO对象列表
        return floors.stream()
                .map(floor -> new FloorDTO(
                        floor.getId(),
                        floor.getFloorNumber(),
                        floor.getPlan(),  // 对应 FloorDTO 中的 floorPlan
                        floor.getHeight(),
                        floor.getLoad(),
                        floor.getMechanicalInfo(),  // 对应 FloorDTO 中的 mechanicalSupport
                        floor.getConstructionDate(),
                        floor.getRenovationDate()
                ))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public BuildingZoneAggregationDTO getBuildingZoneAggregation(Long buildingId, Integer functionId) {
        // 验证建筑是否存在
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found with id: " + buildingId));

        // 验证功能分区是否存在
        ZoneFunction zoneFunction = zoneFunctionRepository.findById(functionId)
                .orElseThrow(() -> new IllegalArgumentException("Zone function not found with id: " + functionId));

        // 获取符合条件的房间列表
        List<Room> rooms = buildingRepository.findRoomsByBuildingIdAndFunctionId(buildingId, functionId);

        // 转换为DTO
        List<BuildingZoneAggregationDTO.RoomBasicDTO> roomDTOs = rooms.stream()
                .map(room -> new BuildingZoneAggregationDTO.RoomBasicDTO(
                        room.getRoomId(),
                        room.getRoomNumber(),
                        room.getRoomName(),
                        room.getFloor().getFloorNumber()  // 正确引用 Room 的 floor 属性
                ))
                .collect(Collectors.toList());

        return new BuildingZoneAggregationDTO(
                zoneFunction.getFunctionId(),
                zoneFunction.getFunctionName(),
                rooms.size(),
                roomDTOs
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ZoneFunctionDTO> getAvailableZoneFunctions() {
        return zoneFunctionRepository.findAll().stream()
            .map(zf -> new ZoneFunctionDTO(zf.getFunctionId(), zf.getFunctionName()))
            .collect(Collectors.toList());
    }
} 