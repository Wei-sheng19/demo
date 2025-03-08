package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.security.RequirePermission;
import com.example.demo.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {
    
    @Autowired
    private MaintenanceService maintenanceService;
    
    @RequirePermission(resource = "maintenance", operation = "read", field = "records")
    @GetMapping("/room/{roomId}/records")
    public ResponseEntity<ApiResponse<?>> getMaintenanceRecords(@PathVariable Long roomId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Maintenance records retrieved successfully",
                maintenanceService.getMaintenanceRecords(roomId)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }
    
    @RequirePermission(resource = "maintenance", operation = "read", field = "feedback")
    @GetMapping("/room/{roomId}/feedback")
    public ResponseEntity<ApiResponse<?>> getFeedbackRecords(@PathVariable Long roomId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Feedback records retrieved successfully",
                maintenanceService.getFeedbackRecords(roomId)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }
    
    @RequirePermission(resource = "maintenance", operation = "read", field = "audit_info")
    @GetMapping("/{maintenanceId}/standardStatus")
    public ResponseEntity<ApiResponse<?>> getAuditInfo(@PathVariable Long maintenanceId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Audit information retrieved successfully",
                maintenanceService.getStandardStatus(maintenanceId)
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