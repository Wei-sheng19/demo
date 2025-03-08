package com.example.demo.service.impl;

import com.example.demo.dao.ConstructionInfoRepository;
import com.example.demo.dao.MaterialEquipmentRepository;
import com.example.demo.dao.RoomRepository;
import com.example.demo.dto.construction.AuditInfoUpdateDTO;
import com.example.demo.dto.construction.ConstructionInfoCreateDTO;
import com.example.demo.dto.construction.ConstructionInfoDTO;
import com.example.demo.dto.construction.ConstructionInfoUpdateDTO;
import com.example.demo.dto.equipment.MaterialEquipmentDTO;
import com.example.demo.dto.equipment.MaterialEquipmentWithLocationDTO;
import com.example.demo.entity.ConstructionInfo;
import com.example.demo.entity.MaterialEquipment;
import com.example.demo.entity.Room;
import com.example.demo.service.ConstructionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConstructionInfoServiceImpl implements ConstructionInfoService {

    @Autowired
    private ConstructionInfoRepository constructionInfoRepository;
    
    @Autowired
    private MaterialEquipmentRepository materialEquipmentRepository;
    
    @Autowired
    private RoomRepository roomRepository;

    /**
     * 创建并保存一个新的施工信息记录。
     * 该方法首先根据提供的房间ID查找对应的房间实体，若未找到则抛出异常。
     * 然后，根据传入的创建DTO对象初始化施工信息实体，并设置相关属性。
     * 最后，保存施工信息实体并返回其DTO表示。
     *
     * @param createDTO 包含施工信息创建所需数据的DTO对象，不能为null
     * @return 返回创建的施工信息的DTO表示
     * @throws IllegalArgumentException 如果根据提供的房间ID未找到对应的房间实体
     */
    @Override
    @Transactional
    public ConstructionInfoDTO createConstructionInfo(ConstructionInfoCreateDTO createDTO) {
        // 根据房间ID查找房间实体，若未找到则抛出异常
        Room room = roomRepository.findById(createDTO.roomId())
            .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + createDTO.roomId()));

        // 初始化施工信息实体并设置相关属性
        ConstructionInfo constructionInfo = new ConstructionInfo();
        constructionInfo.setRoom(room);
        constructionInfo.setProjectName(createDTO.projectName());
        constructionInfo.setBasicConstructionInfo(createDTO.basicConstructionInfo());
        constructionInfo.setArchivalInfo(createDTO.archivalInfo());
        constructionInfo.setMaintenanceStandardStatus(createDTO.maintenanceStandardStatus());
        constructionInfo.setBuildingBasicInfo(createDTO.buildingBasicInfo());
        constructionInfo.setCreatedAt(new Date());
        constructionInfo.setAuditStatus(ConstructionInfo.AuditStatus.PENDING);

        // 保存施工信息实体并返回其DTO表示
        ConstructionInfo saved = constructionInfoRepository.save(constructionInfo);
        return convertToDTO(saved);
    }

    /**
     * 更新指定ID的施工信息。
     * 该方法首先根据提供的ID查找施工信息，如果找不到则抛出异常。
     * 然后根据传入的更新DTO对象，逐个检查并更新施工信息的各个字段。
     * 最后保存更新后的施工信息，并将其转换为DTO对象返回。
     *
     * @param id 要更新的施工信息的唯一标识符
     * @param updateDTO 包含更新信息的DTO对象
     * @return 更新后的施工信息DTO对象
     * @throws IllegalArgumentException 如果找不到指定ID的施工信息
     */
    @Override
    @Transactional
    public ConstructionInfoDTO updateConstructionInfo(Long id, ConstructionInfoUpdateDTO updateDTO) {
        // 根据ID查找施工信息，如果找不到则抛出异常
        ConstructionInfo constructionInfo = constructionInfoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Construction info not found with id: " + id));

        // 逐个检查并更新施工信息的字段
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

        // 保存更新后的施工信息，并将其转换为DTO对象返回
        ConstructionInfo saved = constructionInfoRepository.save(constructionInfo);
        return convertToDTO(saved);
    }

    /**
     * 删除施工信息（逻辑删除）
     *
     * 该方法通过将施工信息的 `isDeleted` 字段设置为 `true` 来实现逻辑删除，而不是从数据库中物理删除记录。
     *
     * @param id 要删除的施工信息的唯一标识符
     * @throws IllegalArgumentException 如果找不到与给定 `id` 对应的施工信息，则抛出此异常
     */
    @Override
    @Transactional
    public void deleteConstructionInfo(Long id) {
        // 根据ID查找施工信息，如果找不到则抛出异常
        ConstructionInfo constructionInfo = constructionInfoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Construction info not found with id: " + id));

        // 将施工信息的 `isDeleted` 字段设置为 `true`，表示逻辑删除
        constructionInfo.setIsDeleted(true);

        // 保存更新后的施工信息到数据库
        constructionInfoRepository.save(constructionInfo);
    }

    /**
     * 根据建筑ID获取施工信息列表。
     * 该函数通过查询数据库获取与指定建筑ID相关的施工信息，并将其转换为DTO对象列表返回。
     *
     * @param buildingId 建筑的唯一标识符，用于查询相关的施工信息。
     * @return 返回一个包含施工信息的DTO对象列表，如果未找到相关施工信息则返回空列表。
     */
    @Override
    @Transactional(readOnly = true)
    public List<ConstructionInfoDTO> getConstructionInfoByBuildingId(Long buildingId) {
        // 通过建筑ID从数据库中查询施工信息，并将其转换为DTO对象列表
        return constructionInfoRepository.findByBuildingId(buildingId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * 根据楼层ID获取施工信息列表。
     * 该函数通过查询数据库获取指定楼层的施工信息，并将其转换为DTO对象列表返回。
     * 该操作为只读事务，确保数据在查询过程中不会被修改。
     *
     * @param floorId 楼层ID，用于查询指定楼层的施工信息。
     * @return 返回一个包含施工信息的DTO对象列表，如果未找到相关数据则返回空列表。
     */
    @Override
    @Transactional(readOnly = true)
    public List<ConstructionInfoDTO> getConstructionInfoByFloorId(Long floorId) {
        // 通过楼层ID从仓库中查询施工信息，并将其流化以便进行后续处理
        return constructionInfoRepository.findByFloorId(floorId)
            .stream()
            // 将每个施工信息实体转换为DTO对象
            .map(this::convertToDTO)
            // 将流中的DTO对象收集到列表中并返回
            .collect(Collectors.toList());
    }

    /**
     * 更新施工信息的审核信息。
     * 该函数根据提供的ID查找施工信息，并更新其审核信息和审核状态，最后保存更新后的信息并返回DTO对象。
     *
     * @param id 施工信息的唯一标识符，用于查找需要更新的施工信息。
     * @param auditDTO 包含更新审核信息和审核状态的数据传输对象。
     * @return 更新后的施工信息DTO对象。
     * @throws IllegalArgumentException 如果未找到与ID对应的施工信息，则抛出此异常。
     */
    @Override
    @Transactional
    public ConstructionInfoDTO updateAuditInfo(Long id, AuditInfoUpdateDTO auditDTO) {
        // 根据ID查找施工信息，如果未找到则抛出异常
        ConstructionInfo constructionInfo = constructionInfoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Construction info not found with id: " + id));

        // 更新施工信息的审核信息、审核状态和更新时间
        constructionInfo.setAuditInfo(auditDTO.auditInfo());
        constructionInfo.setAuditStatus(auditDTO.auditStatus());
        constructionInfo.setUpdatedAt(new Date());

        // 保存更新后的施工信息并转换为DTO对象返回
        ConstructionInfo saved = constructionInfoRepository.save(constructionInfo);
        return convertToDTO(saved);
    }

    /**
     * 根据房间ID获取房间的施工信息。
     * 该函数首先通过房间ID从数据库中查找对应的房间实体，如果房间不存在则抛出异常。
     * 然后从房间实体中获取施工信息，如果施工信息不存在则抛出异常。
     * 最后将施工信息转换为DTO对象并返回。
     *
     * @param roomId 房间的唯一标识符，不能为null。
     * @return 返回包含施工信息的ConstructionInfoDTO对象。
     * @throws IllegalArgumentException 如果房间不存在或施工信息不存在，则抛出此异常。
     */
    @Override
    @Transactional(readOnly = true)
    public ConstructionInfoDTO getRoomConstructionInfo(Long roomId) {
        // 根据房间ID查找房间实体，如果不存在则抛出异常
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));

        // 从房间实体中获取施工信息，如果不存在则抛出异常
        ConstructionInfo constructionInfo = room.getConstructionInfo();
        if (constructionInfo == null) {
            throw new IllegalArgumentException("Construction info not found for room: " + roomId);
        }

        // 将施工信息转换为DTO对象并返回
        return convertToDTO(constructionInfo);
    }

    /**
     * 根据房间ID获取该房间的所有材料和设备信息。
     * 该函数会从数据库中查询指定房间，并将其关联的材料和设备信息转换为DTO对象列表返回。
     *
     * @param roomId 房间的唯一标识符，不能为null。
     * @return 包含房间所有材料和设备信息的DTO对象列表。如果房间不存在，则抛出IllegalArgumentException异常。
     * @throws IllegalArgumentException 如果指定的房间ID在数据库中不存在。
     */
    @Override
    @Transactional(readOnly = true)
    public List<MaterialEquipmentDTO> getRoomMaterialsAndEquipments(Long roomId) {
        // 根据房间ID从数据库中查询房间信息，如果房间不存在则抛出异常
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));

        // 将房间关联的材料和设备信息转换为DTO对象，并收集为列表返回
        return room.getMaterialEquipments().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * 获取指定楼层和物料名称的设备详细信息。
     *
     * 该函数通过调用物料设备仓库的查询方法，根据楼层ID和物料名称检索设备信息，
     * 并返回包含设备位置信息的DTO列表。该操作为只读事务，确保数据查询的效率和一致性。
     *
     * @param floorId 楼层ID，用于指定查询的设备所在楼层。
     * @param materialName 物料名称，用于指定查询的设备所属物料。
     * @return 包含设备位置信息的DTO列表，列表中的每个元素代表一个设备及其位置信息。
     */
    @Override
    @Transactional(readOnly = true)
    public List<MaterialEquipmentWithLocationDTO> getFloorSpecificEquipments(Long floorId, String materialName) {
        return materialEquipmentRepository.findDetailsByFloorIdAndMaterialName(floorId, materialName);
    }

    /**
     * 获取指定房间内特定设备的详细信息。
     * 该函数首先根据房间ID查找房间，然后在房间的设备列表中查找指定ID的设备，
     * 并将其转换为DTO对象返回。如果房间或设备未找到，则抛出异常。
     *
     * @param roomId 房间的唯一标识符，不能为null。
     * @param equipmentId 设备的唯一标识符，不能为null。
     * @return MaterialEquipmentDTO 包含设备详细信息的DTO对象。
     * @throws IllegalArgumentException 如果房间或设备未找到，则抛出此异常。
     */
    @Override
    @Transactional(readOnly = true)
    public MaterialEquipmentDTO getRoomEquipmentDetails(Long roomId, Long equipmentId) {
        // 根据房间ID查找房间，如果未找到则抛出异常
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));

        // 在房间的设备列表中查找指定ID的设备，并将其转换为DTO对象返回
        return room.getMaterialEquipments().stream()
            .filter(eq -> eq.getMaterialEquipmentId().equals(equipmentId))
            .findFirst()
            .map(this::convertToDTO)
            .orElseThrow(() -> new IllegalArgumentException(
                "Equipment not found with id: " + equipmentId + " in room: " + roomId));
    }

    /**
     * 根据楼层ID获取该楼层下的所有物料和设备信息，并返回包含位置信息的DTO列表。
     * 该方法是只读的，使用了Spring的@Transactional注解来确保事务的只读性。
     *
     * @param floorId 楼层ID，用于查询指定楼层的物料和设备信息。
     * @return 返回一个包含物料和设备信息的DTO列表，每个DTO都包含位置信息。
     */
    @Override
    @Transactional(readOnly = true)
    public List<MaterialEquipmentWithLocationDTO> getFloorMaterialsAndEquipments(Long floorId) {
        // 调用仓库层的findDistinctDetailsByFloorId方法，根据楼层ID查询物料和设备信息
        return materialEquipmentRepository.findDistinctDetailsByFloorId(floorId);
    }

    /**
     * 根据建筑ID获取该建筑下的所有物料和设备信息，并返回包含位置信息的DTO列表。
     * 该方法是只读操作，确保在事务中执行，不会对数据库进行写操作。
     *
     * @param buildingId 建筑的唯一标识符，用于查询该建筑下的物料和设备信息。
     * @return 返回一个包含物料和设备信息的DTO列表，每个DTO都包含位置信息。
     */
    @Override
    @Transactional(readOnly = true)
    public List<MaterialEquipmentWithLocationDTO> getBuildingMaterialsAndEquipments(Long buildingId) {
        // 通过物料设备仓库查询指定建筑ID下的所有物料和设备详细信息，并返回结果。
        return materialEquipmentRepository.findDistinctDetailsByBuildingId(buildingId);
    }

    /**
     * 更新房间的运维标准状态。
     * 该方法首先查找房间的建设信息，然后更新其运维标准状态。
     *
     * @param roomId 房间ID
     * @param standardInfo 包含运维标准状态信息的Map
     * @return 更新后的建设信息DTO
     * @throws IllegalArgumentException 如果房间不存在或房间没有关联的建设信息
     */
    @Override
    @Transactional
    public ConstructionInfoDTO updateMaintenanceStandard(Long roomId, Map<String, String> standardInfo) {
        // 查找房间
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));

        // 获取房间的建设信息
        ConstructionInfo constructionInfo = room.getConstructionInfo();
        if (constructionInfo == null) {
            throw new IllegalArgumentException("Construction info not found for room: " + roomId);
        }

        // 更新运维标准状态
        if (standardInfo.containsKey("maintenanceStandardStatus")) {
            constructionInfo.setMaintenanceStandardStatus(standardInfo.get("maintenanceStandardStatus"));
            constructionInfo.setUpdatedAt(new Date());
        }

        // 保存更新
        ConstructionInfo saved = constructionInfoRepository.save(constructionInfo);
        return convertToDTO(saved);
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