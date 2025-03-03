package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class ImportResponse {
    private boolean success;
    private String message;
    private int totalRecords;
    private int successCount;
    private int failureCount;
    private List<String> errors;
    
    public static ImportResponse success(int total, int success) {
        ImportResponse response = new ImportResponse();
        response.setSuccess(true);
        response.setMessage("Import completed successfully");
        response.setTotalRecords(total);
        response.setSuccessCount(success);
        response.setFailureCount(total - success);
        return response;
    }
    
    public static ImportResponse failure(String message, List<String> errors) {
        ImportResponse response = new ImportResponse();
        response.setSuccess(false);
        response.setMessage(message);
        response.setErrors(errors);
        return response;
    }
} 