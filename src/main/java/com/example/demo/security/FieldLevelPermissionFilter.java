package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 字段级权限过滤器，用于根据用户权限动态过滤返回数据的字段
 */
@Component
public class FieldLevelPermissionFilter {

    @Autowired
    private CustomPermissionEvaluator permissionEvaluator;
    
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 根据用户权限过滤对象字段
     * 
     * @param data 原始数据对象
     * @param authentication 用户认证信息
     * @param resource 资源类型
     * @return 过滤后的数据（只包含用户有权访问的字段）
     */
    public <T> Object filterFields(T data, Authentication authentication, String resource) {
        if (data == null) {
            return null;
        }
        
        try {
            // 使用Jackson将对象转换为Map
            Map<String, Object> dataMap = objectMapper.convertValue(data, Map.class);
            Map<String, Object> filteredMap = new HashMap<>();
            
            // 检查每个字段的权限
            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                String field = entry.getKey();
                String requiredPermission = resource + ":read:" + field;
                
                // 如果用户有权限访问该字段，则保留它
                if (permissionEvaluator.hasPermission(authentication, resource, "read", field)) {
                    filteredMap.put(field, entry.getValue());
                }
            }
            
            // 将过滤后的Map转换回ObjectNode
            return objectMapper.convertValue(filteredMap, ObjectNode.class);
            
        } catch (Exception e) {
            // 如果转换失败，返回原始数据
            return data;
        }
    }
    
    /**
     * 使用反射过滤对象字段（替代方法，更适合复杂对象）
     */
    public <T> T filterFieldsWithReflection(T data, Authentication authentication, String resource) {
        if (data == null) {
            return null;
        }
        
        try {
            // 创建目标类的新实例
            @SuppressWarnings("unchecked")
            T result = (T) data.getClass().getDeclaredConstructor().newInstance();
            
            // 获取所有字段
            Field[] fields = data.getClass().getDeclaredFields();
            
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                
                // 检查用户是否有权限访问该字段
                if (permissionEvaluator.hasPermission(authentication, resource, "read", fieldName)) {
                    // 复制有权限访问的字段
                    field.set(result, field.get(data));
                }
            }
            
            return result;
            
        } catch (Exception e) {
            // 如果反射操作失败，返回原始数据
            return data;
        }
    }
} 