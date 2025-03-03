package com.example.demo.validator;

import java.util.List;
import java.util.Map;

public interface DataValidator {
    /**
     * 验证数据是否符合要求
     * @param data 待验证的数据
     * @return 验证错误信息列表，如果为空则表示验证通过
     */
    List<String> validate(Map<String, String> data);
    
    /**
     * 获取必需的字段列表
     * @return 必需的字段名称列表
     */
    List<String> getRequiredFields();
    
    /**
     * 验证字段类型
     * @param fieldName 字段名
     * @param value 字段值
     * @return 是否符合类型要求
     */
    boolean validateFieldType(String fieldName, String value);
} 