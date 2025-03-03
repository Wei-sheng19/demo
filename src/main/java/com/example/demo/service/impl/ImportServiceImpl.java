package com.example.demo.service.impl;

import com.example.demo.dto.ImportRequest;
import com.example.demo.dto.ImportResponse;
import com.example.demo.service.DataImportHandler;
import com.example.demo.service.ImportService;
import com.example.demo.strategy.ImportStrategy;
import com.example.demo.validator.ConsistencyValidator;
import com.example.demo.validator.DataValidator;
import com.example.demo.validator.DependencyValidator;
import com.example.demo.validator.ForeignKeyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImportServiceImpl implements ImportService {
    
    @Autowired
    private ImportStrategy csvImportStrategy;
    
    @Autowired
    private Map<String, DataValidator> validators;
    
    @Autowired
    private Map<String, DataImportHandler<?>> importHandlers;
    
    @Autowired
    private DependencyValidator dependencyValidator;
    
    @Autowired
    private ForeignKeyValidator foreignKeyValidator;
    
    @Autowired
    private ConsistencyValidator consistencyValidator;
    
    @Autowired
    private List<String> entityImportOrder;
    
    @Override
    @Transactional
    public ImportResponse importData(ImportRequest request) {
        try {
            // 验证文件格式
            if (!csvImportStrategy.validateFileFormat(request.getFile())) {
                return ImportResponse.failure("Invalid file format. Only CSV files are supported.", null);
            }
            
            // 验证依赖关系
            if (!dependencyValidator.canImport(request.getEntityType())) {
                return ImportResponse.failure(
                    "Dependencies not satisfied. Required entities must be imported first: " + 
                    String.join(", ", dependencyValidator.validateDependencies(request.getEntityType(), null)),
                    null
                );
            }
            
            // 获取对应的验证器和处理器
            DataValidator validator = validators.get(request.getEntityType());
            DataImportHandler<?> handler = importHandlers.get(request.getEntityType());
            
            if (validator == null || handler == null) {
                return ImportResponse.failure("Unsupported entity type: " + request.getEntityType(), null);
            }
            
            // 解析文件
            List<Map<String, String>> records = csvImportStrategy.parseFile(request.getFile());
            
            // 数据一致性验证
            List<String> consistencyErrors = consistencyValidator.validateConsistency(request.getEntityType(), records);
            if (!consistencyErrors.isEmpty()) {
                return ImportResponse.failure("Data consistency validation failed", consistencyErrors);
            }
            
            // 验证和导入数据
            List<String> errors = new ArrayList<>();
            int successCount = 0;
            
            for (Map<String, String> record : records) {
                List<String> validationErrors = new ArrayList<>();
                
                // 1. 数据格式验证
                validationErrors.addAll(validator.validate(record));
                
                // 2. 依赖关系验证
                validationErrors.addAll(dependencyValidator.validateDependencies(request.getEntityType(), record));
                
                // 3. 外键验证
                validationErrors.addAll(foreignKeyValidator.validateForeignKeys(request.getEntityType(), record));
                
                // 4. 单条记录一致性验证
                validationErrors.addAll(consistencyValidator.validateRecordConsistency(request.getEntityType(), record));
                
                if (validationErrors.isEmpty()) {
                    try {
                        // 使用泛型通配符捕获来处理类型转换
                        @SuppressWarnings("unchecked")
                        DataImportHandler<Object> typedHandler = (DataImportHandler<Object>) handler;
                        Object entity = typedHandler.convertToEntity(record);
                        typedHandler.saveOrUpdate(entity, request.isUpdateExisting());
                        successCount++;
                    } catch (Exception e) {
                        errors.add("Failed to import record: " + e.getMessage());
                    }
                } else {
                    errors.addAll(validationErrors);
                }
            }
            
            if (errors.isEmpty()) {
                // 标记该实体类型已导入
                dependencyValidator.markAsImported(request.getEntityType());
                return ImportResponse.success(records.size(), successCount);
            } else {
                return ImportResponse.failure("Some records failed to import", errors);
            }
            
        } catch (Exception e) {
            return ImportResponse.failure("Import failed: " + e.getMessage(), null);
        }
    }
    
    @Override
    @Transactional
    public List<ImportResponse> importDataBatch(List<ImportRequest> requests) {
        List<ImportResponse> responses = new ArrayList<>();
        dependencyValidator.reset();  // 重置导入状态
        
        // 按照依赖顺序处理请求
        Map<String, ImportRequest> requestMap = new HashMap<>();
        requests.forEach(request -> requestMap.put(request.getEntityType(), request));
        
        for (String entityType : entityImportOrder) {
            ImportRequest request = requestMap.get(entityType);
            if (request != null) {
                responses.add(importData(request));
            }
        }
        
        return responses;
    }
    
    @Override
    public List<String> getRecommendedImportOrder() {
        return entityImportOrder;
    }
}