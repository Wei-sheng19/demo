package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.security.RequirePermission;
import com.example.demo.service.SubstationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/substation")
public class SubstationController {
    
    @Autowired
    private SubstationService substationService;
    
    @RequirePermission(resource = "substation", operation = "read", field = "basic_info")
    @GetMapping("/{substationId}/basicInfo")
    public ResponseEntity<ApiResponse<?>> getSubstationInfo(@PathVariable Long substationId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Substation information retrieved successfully",
                substationService.getSubstationInfo(substationId)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }
    
    @RequirePermission(resource = "substation", operation = "read", field = "buildings")
    @GetMapping("/{substationId}/buildings")
    public ResponseEntity<ApiResponse<?>> getSupplyBuildings(@PathVariable Long substationId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Supply buildings retrieved successfully",
                substationService.getSupplyBuildings(substationId)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }
    
    @RequirePermission(resource = "substation", operation = "read", field = "power_rooms")
    @GetMapping("/{substationId}/powerRooms")
    public ResponseEntity<ApiResponse<?>> getSubPowerRooms(@PathVariable Long substationId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Sub power rooms retrieved successfully",
                substationService.getSubPowerRooms(substationId)
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