package com.example.demo.validator;

import java.util.List;
import java.util.Map;

/**
 * 外键关系验证器接口
 */
public interface ForeignKeyValidator {
    /**
     * 验证外键引用的有效性
     * @param entityType 实体类型
     * @param data CSV记录数据
     * @return 验证错误信息列表
     */
    List<String> validateForeignKeys(String entityType, Map<String, String> data);
} 