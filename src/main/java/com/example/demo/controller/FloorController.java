package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.security.RequirePermission;
import com.example.demo.service.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/floor")
public class FloorController {
    
    @Autowired
    private FloorService floorService;
    
    @RequirePermission(resource = "floor", operation = "read", field = "basic_info")
    @GetMapping("/{floorId}/basicInfo")
    public ResponseEntity<ApiResponse<?>> getFloorInfo(@PathVariable Long floorId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Floor information retrieved successfully",
                floorService.getFloorInfo(floorId)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }
    
    @RequirePermission(resource = "floor", operation = "read", field = "rooms")
    @GetMapping("/{floorId}/rooms")
    public ResponseEntity<ApiResponse<?>> getFloorRooms(@PathVariable Long floorId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Floor rooms retrieved successfully",
                floorService.getFloorRooms(floorId)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }
    
    @RequirePermission(resource = "floor", operation = "read", field = "power_room")
    @GetMapping("/{floorId}/powerRoom")
    public ResponseEntity<ApiResponse<?>> getFloorPowerRoom(@PathVariable Long floorId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Floor power room information retrieved successfully",
                floorService.getFloorPowerRoom(floorId)
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
    @GetMapping("/building/{buildingId}")
    public ResponseEntity<ApiResponse<?>> getBuildingFloors(@PathVariable Long buildingId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Building floors retrieved successfully",
                floorService.getBuildingFloors(buildingId)
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