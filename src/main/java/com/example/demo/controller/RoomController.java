package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.security.RequirePermission;
import com.example.demo.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    
    @Autowired
    private RoomService roomService;
    
    @RequirePermission(resource = "room", operation = "read", field = "basic_info")
    @GetMapping("/{roomId}/basicInfo")
    public ResponseEntity<ApiResponse<?>> getRoomBasicInfo(@PathVariable Long roomId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Room information retrieved successfully",
                roomService.getRoomBasicInfo(roomId)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }
    
    @RequirePermission(resource = "room", operation = "read", field = "function")
    @GetMapping("/{roomId}/currentFunction")
    public ResponseEntity<ApiResponse<?>> getCurrentFunction(@PathVariable Long roomId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Room function information retrieved successfully",
                roomService.getCurrentFunction(roomId)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }
    
    @RequirePermission(resource = "room", operation = "read", field = "details")
    @GetMapping("/{roomId}/details")
    public ResponseEntity<ApiResponse<?>> getRoomDetails(@PathVariable Long roomId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Room details retrieved successfully",
                roomService.getRoomDetails(roomId)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }

    @RequirePermission(resource = "room", operation = "update", field = "site_audit")
    @PutMapping("/{roomId}/site-audit")
    public ResponseEntity<ApiResponse<?>> updateSiteAudit(
            @PathVariable Long roomId,
            @RequestBody Map<String, String> auditInfo) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Room site audit information updated successfully",
                roomService.updateSiteAudit(roomId, auditInfo)
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