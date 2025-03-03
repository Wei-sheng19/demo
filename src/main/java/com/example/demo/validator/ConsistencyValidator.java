package com.example.demo.validator;

import java.util.List;
import java.util.Map;

/**
 * 数据一致性验证器接口
 */
public interface ConsistencyValidator {
    /**
     * 验证数据一致性
     * @param entityType 实体类型
     * @param records 待验证的记录列表
     * @return 验证错误信息列表
     */
    List<String> validateConsistency(String entityType, List<Map<String, String>> records);
    
    /**
     * 验证单条记录的数据一致性
     * @param entityType 实体类型
     * @param record 待验证的记录
     * @return 验证错误信息列表
     */
    List<String> validateRecordConsistency(String entityType, Map<String, String> record);
}