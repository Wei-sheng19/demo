package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.security.RequirePermission;
import com.example.demo.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/building")
public class BuildingController {
    
    @Autowired
    private BuildingService buildingService;
    //获取建筑基本信息
    @RequirePermission(resource = "building", operation = "read", field = "basic_info")
    @GetMapping("/{buildingId}/basicInfo")
    public ResponseEntity<ApiResponse<?>> getBuildingBasicInfo(@PathVariable Long buildingId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Building information retrieved successfully",
                buildingService.getBuildingBasicInfo(buildingId)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }
    //获取建筑土地档案
    @RequirePermission(resource = "building", operation = "read", field = "land_archives")
    @GetMapping("/{buildingId}/landArchives")
    public ResponseEntity<ApiResponse<?>> getLandArchives(@PathVariable Long buildingId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Land archives retrieved successfully",
                buildingService.getLandArchives(buildingId)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }
    //获取地下室信息
    @RequirePermission(resource = "building", operation = "read", field = "basement_info")
    @GetMapping("/{buildingId}/basementInfo")
    public ResponseEntity<ApiResponse<?>> getBasementInfo(@PathVariable Long buildingId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Basement information retrieved successfully",
                buildingService.getBasementInfo(buildingId)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }

    @RequirePermission(resource = "building", operation = "read", field = "floors")
    @GetMapping("/{buildingId}/floors")
    public ResponseEntity<ApiResponse<?>> getBuildingFloors(@PathVariable Long buildingId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Building floors retrieved successfully",
                buildingService.getBuildingFloors(buildingId)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }
    //根据功能id获取该建筑该功能的所有房间
    @RequirePermission(resource = "building", operation = "read", field = "zones")
    @GetMapping("/{buildingId}/zones/{functionId}")
    public ResponseEntity<ApiResponse<?>> getBuildingZoneAggregation(
            @PathVariable Long buildingId,
            @PathVariable Integer functionId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Building zone aggregation retrieved successfully",
                buildingService.getBuildingZoneAggregation(buildingId, functionId)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }
    //获取所有可用的功能分区种类
    @RequirePermission(resource = "zone_function", operation = "read", field = "*")
    @GetMapping("/zones/available")
    public ResponseEntity<ApiResponse<?>> getAvailableZoneFunctions() {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Available zone functions retrieved successfully",
                buildingService.getAvailableZoneFunctions()
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