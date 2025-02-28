package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.security.RequirePermission;
import com.example.demo.service.CampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campus")
public class CampusController {
    
    @Autowired
    private CampusService campusService;
    
    @RequirePermission(resource = "campus", operation = "read", field = "basic_info")
    @GetMapping("/{campusId}")
    public ResponseEntity<ApiResponse<?>> getCampusInfo(@PathVariable Long campusId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                "Campus information retrieved successfully",
                campusService.getCampusInfo(campusId)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                e.getMessage(),
                null
            ));
        }
    }
    
    @RequirePermission(resource = "campus", operation = "read", field = "basic_info")
    @GetMapping("/campusname/{campusName}")
    public ResponseEntity<ApiResponse<?>> getCampusInfoByName(@PathVariable String campusName) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(
                    "SUCCESS",
                    "Campus information retrieved successfully",
                    campusService.getCampusInfoByName(campusName)
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