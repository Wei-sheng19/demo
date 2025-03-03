package com.example.demo.validator;

import java.util.List;
import java.util.Map;

/**
 * 依赖关系验证器接口
 */
public interface DependencyValidator {
    /**
     * 验证指定实体类型的依赖关系
     * @param entityType 实体类型
     * @param data 实体数据
     * @return 验证错误信息列表
     */
    List<String> validateDependencies(String entityType, Map<String, String> data);
    
    /**
     * 检查指定实体类型是否可以导入
     * @param entityType 实体类型
     * @return 是否可以导入
     */
    boolean canImport(String entityType);
    
    /**
     * 标记指定实体类型已完成导入
     * @param entityType 实体类型
     */
    void markAsImported(String entityType);
    
    /**
     * 重置所有实体的导入状态
     */
    void reset();
} 