package com.example.demo.validator.impl;

import com.example.demo.validator.DataValidator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component("campus")
public class CampusValidator implements DataValidator {
    
    private static final List<String> REQUIRED_FIELDS = Arrays.asList(
        "name",
        "location"
    );
    
    @Override
    public List<String> validate(Map<String, String> data) {
        List<String> errors = new ArrayList<>();
        
        // 检查必需字段
        for (String field : REQUIRED_FIELDS) {
            if (!data.containsKey(field) || data.get(field).trim().isEmpty()) {
                errors.add("Missing required field: " + field);
            }
        }
        
        // 验证字段类型和格式
        data.forEach((field, value) -> {
            if (value != null && !value.trim().isEmpty() && !validateFieldType(field, value)) {
                errors.add("Invalid value for field: " + field);
            }
        });
        
        return errors;
    }
    
    @Override
    public List<String> getRequiredFields() {
        return REQUIRED_FIELDS;
    }
    
    @Override
    public boolean validateFieldType(String fieldName, String value) {
        switch (fieldName) {
            case "name":
            case "location":
            case "floorPlan":
            case "threeDModel":
                return true; // 这些都是字符串类型，不需要特殊验证
            default:
                return false;
        }
    }
} 