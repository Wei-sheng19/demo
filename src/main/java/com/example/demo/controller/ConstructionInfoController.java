package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.*;
import com.example.demo.security.RequirePermission;
import com.example.demo.service.ConstructionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/construction-info")
public class ConstructionInfoController {
    
    @Autowired
    private ConstructionInfoService constructionInfoService;

    @PostMapping
    @RequirePermission(resource = "construction", operation = "create", field = "*")
    public ResponseEntity<ApiResponse<?>> createConstructionInfo(@RequestBody ConstructionInfoCreateDTO createDTO) {
        try {
            ConstructionInfoDTO result = constructionInfoService.createConstructionInfo(createDTO);
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Construction info created successfully",
                result
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }

    @PutMapping("/{id}")
    @RequirePermission(resource = "construction", operation = "update", field = "basic_info")
    public ResponseEntity<ApiResponse<?>> updateConstructionInfo(
            @PathVariable Long id,
            @RequestBody ConstructionInfoUpdateDTO updateDTO) {
        try {
            ConstructionInfoDTO result = constructionInfoService.updateConstructionInfo(id, updateDTO);
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Construction info updated successfully",
                result
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }

    @DeleteMapping("/{id}")
    @RequirePermission(resource = "construction", operation = "delete", field = "*")
    public ResponseEntity<ApiResponse<?>> deleteConstructionInfo(@PathVariable Long id) {
        try {
            constructionInfoService.deleteConstructionInfo(id);
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Construction info deleted successfully",
                null
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }

    @GetMapping("/building/{buildingId}")
    @RequirePermission(resource = "construction", operation = "read", field = "basic_info")
    public ResponseEntity<ApiResponse<?>> getConstructionInfoByBuilding(@PathVariable Long buildingId) {
        try {
            List<ConstructionInfoDTO> result = constructionInfoService.getConstructionInfoByBuildingId(buildingId);
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Construction info retrieved successfully",
                result
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }

    @GetMapping("/floor/{floorId}")
    @RequirePermission(resource = "construction", operation = "read", field = "basic_info")
    public ResponseEntity<ApiResponse<?>> getConstructionInfoByFloor(@PathVariable Long floorId) {
        try {
            List<ConstructionInfoDTO> result = constructionInfoService.getConstructionInfoByFloorId(floorId);
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Construction info retrieved successfully",
                result
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }

    @PutMapping("/{id}/audit")
    @RequirePermission(resource = "construction", operation = "update", field = "audit_info")
    public ResponseEntity<ApiResponse<?>> updateAuditInfo(
            @PathVariable Long id,
            @RequestBody AuditInfoUpdateDTO auditDTO) {
        try {
            ConstructionInfoDTO result = constructionInfoService.updateAuditInfo(id, auditDTO);
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Audit info updated successfully",
                result
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }

    /**
     * 根据房间ID获取房间的施工信息。
     *
     * @param roomId 房间ID，用于指定查询的房间。
     * @return ResponseEntity<ApiResponse<?>> 包含操作结果的响应实体。如果成功，返回房间的施工信息；如果失败，返回错误信息。
     */
    @RequirePermission(resource = "construction", operation = "read", field = "basic_info")
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse<?>> getRoomConstructionInfo(@PathVariable Long roomId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Room construction info retrieved successfully",
                constructionInfoService.getRoomConstructionInfo(roomId)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }

    /**
     * 根据房间ID获取房间的材料和设备信息。
     *
     * @param roomId 房间ID，用于指定查询的房间。
     * @return ResponseEntity<ApiResponse<?>> 包含操作结果的响应实体。如果成功，返回房间的材料和设备信息；如果失败，返回错误信息。
     */
    @RequirePermission(resource = "equipment", operation = "read", field = "basic_info")
    @GetMapping("/room/{roomId}/materials")
    public ResponseEntity<ApiResponse<?>> getRoomMaterialsAndEquipments(@PathVariable Long roomId) {
        try {
            List<MaterialEquipmentDTO> materials = constructionInfoService.getRoomMaterialsAndEquipments(roomId);
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Room materials and equipments retrieved successfully",
                materials
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }

    /**
     * 根据楼层ID和材料名称获取特定楼层的设备信息。
     *
     * @param floorId 楼层ID，用于指定查询的楼层。
     * @param materialName 材料名称，用于指定查询的设备材料类型。
     * @return ResponseEntity<ApiResponse<?>> 包含操作结果的响应实体。如果成功，返回设备列表；如果失败，返回错误信息。
     */
    @RequirePermission(resource = "equipment", operation = "read", field = "technical_requirements")
    @GetMapping("/floor/{floorId}/equipment/{materialName}")
    public ResponseEntity<ApiResponse<?>> getFloorSpecificEquipments(@PathVariable Long floorId, @PathVariable String materialName) {
        try {
            // 调用服务层方法，获取特定楼层的设备信息
            List<MaterialEquipmentWithLocationDTO> equipments = constructionInfoService.getFloorSpecificEquipments(floorId, materialName);

            // 返回成功响应，包含设备列表
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Floor specific equipments retrieved successfully",
                equipments
            ));
        } catch (Exception e) {
            // 捕获异常并返回错误响应，包含错误信息
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }

    /**
     * 根据房间ID和设备ID获取房间内特定设备的详细信息。
     *
     * @param roomId 房间ID，用于指定查询的房间。
     * @param equipmentId 设备ID，用于指定查询的设备。
     * @return ResponseEntity<ApiResponse<?>> 包含操作结果的响应实体。如果成功，返回设备的详细信息；如果失败，返回错误信息。
     */
    @RequirePermission(resource = "equipment", operation = "read", field = "*")
    @GetMapping("/room/{roomId}/equipment/{equipmentId}")
    public ResponseEntity<ApiResponse<?>> getRoomEquipmentDetails(@PathVariable Long roomId, @PathVariable Long equipmentId) {
        try {
            MaterialEquipmentDTO equipmentDetails = constructionInfoService.getRoomEquipmentDetails(roomId, equipmentId);
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Room equipment details retrieved successfully",
                equipmentDetails
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }

    /**
     * 根据楼层ID获取楼层的材料和设备信息。
     *
     * @param floorId 楼层ID，用于指定查询的楼层。
     * @return ResponseEntity<ApiResponse<?>> 包含操作结果的响应实体。如果成功，返回楼层的材料和设备信息；如果失败，返回错误信息。
     */
    @RequirePermission(resource = "equipment", operation = "read", field = "vendor_info")
    @GetMapping("/floor/{floorId}/materials")
    public ResponseEntity<ApiResponse<?>> getFloorMaterialsAndEquipments(@PathVariable Long floorId) {
        try {
            List<MaterialEquipmentWithLocationDTO> materials = constructionInfoService.getFloorMaterialsAndEquipments(floorId);
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Floor materials and equipments retrieved successfully",
                materials
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }

    /**
     * 根据建筑ID获取建筑的材料和设备信息。
     *
     * @param buildingId 建筑ID，用于指定查询的建筑。
     * @return ResponseEntity<ApiResponse<?>> 包含操作结果的响应实体。如果成功，返回建筑的材料和设备信息；如果失败，返回错误信息。
     */
    @RequirePermission(resource = "equipment", operation = "read", field = "lifecycle_warning")
    @GetMapping("/building/{buildingId}/materials")
    public ResponseEntity<ApiResponse<?>> getBuildingMaterialsAndEquipments(@PathVariable Long buildingId) {
        try {
            List<MaterialEquipmentWithLocationDTO> materials = constructionInfoService.getBuildingMaterialsAndEquipments(buildingId);
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Building materials and equipments retrieved successfully",
                materials
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }

} 