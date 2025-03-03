package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.ImportRequest;
import com.example.demo.dto.ImportResponse;
import com.example.demo.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
public class ImportController {
    
    @Autowired
    private ImportService importService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<?>> importData(
            @RequestParam("file") MultipartFile file,
            @RequestParam("entityType") String entityType,
            @RequestParam(value = "updateExisting", defaultValue = "false") boolean updateExisting) {
        
        try {
            ImportRequest request = new ImportRequest();
            request.setFile(file);
            request.setEntityType(entityType);
            request.setUpdateExisting(updateExisting);
            
            ImportResponse response = importService.importData(request);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(new ApiResponse<>(
                    "SUCCESS",
                    "Data imported successfully",
                    response
                ));
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "ERROR",
                    response.getMessage(),
                    response
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "ERROR",
                "Failed to import data: " + e.getMessage(),
                null
            ));
        }
    }
} 