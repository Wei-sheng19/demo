package com.example.demo.security;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class PermissionExpressionParser {
    
    /**
     * 解析权限表达式并检查是否匹配
     * @param requiredExpression 所需权限的表达式 (例如 "room:read:basic_info")
     * @param userExpression 用户拥有的权限表达式 (例如 "room:read:*" 或 "room:*:*")
     * @return 如果用户权限匹配所需权限，则返回true
     */
    public boolean matches(String requiredExpression, String userExpression) {
        if (requiredExpression == null || userExpression == null) {
            return false;
        }
        
        String[] requiredParts = requiredExpression.split(":");
        String[] userParts = userExpression.split(":");
        
        if (requiredParts.length != 3 || userParts.length != 3) {
            return false;
        }
        
        // 分别检查资源、操作和字段
        return matchesPart(requiredParts[0], userParts[0]) && 
               matchesPart(requiredParts[1], userParts[1]) && 
               matchesPart(requiredParts[2], userParts[2]);
    }
    
    /**
     * 检查单个部分是否匹配（支持通配符和多值）
     */
    private boolean matchesPart(String required, String userHas) {
        // 通配符匹配任何值
        if (userHas.equals("*")) {
            return true;
        }
        
        // 检查逗号分隔的多个值
        if (userHas.contains(",")) {
            String[] values = userHas.split(",");
            for (String value : values) {
                if (matchesSingleValue(required, value.trim())) {
                    return true;
                }
            }
            return false;
        }
        
        return matchesSingleValue(required, userHas);
    }
    
    /**
     * 匹配单个值，支持?作为单字符通配符
     */
    private boolean matchesSingleValue(String required, String userHas) {
        // 直接相等
        if (required.equals(userHas)) {
            return true;
        }
        
        // 转换问号通配符为正则表达式
        if (userHas.contains("?")) {
            String regex = userHas.replace("?", ".");
            return Pattern.matches(regex, required);
        }
        
        return false;
    }
    
    /**
     * 获取权限表达式的资源部分
     */
    public String extractResource(String expression) {
        if (expression == null) {
            return null;
        }
        String[] parts = expression.split(":");
        return parts.length > 0 ? parts[0] : null;
    }
    
    /**
     * 获取权限表达式的操作部分
     */
    public String extractOperation(String expression) {
        if (expression == null) {
            return null;
        }
        String[] parts = expression.split(":");
        return parts.length > 1 ? parts[1] : null;
    }
    
    /**
     * 获取权限表达式的字段部分
     */
    public String extractField(String expression) {
        if (expression == null) {
            return null;
        }
        String[] parts = expression.split(":");
        return parts.length > 2 ? parts[2] : null;
    }
    
    /**
     * 从多个权限表达式中找出匹配的
     */
    public boolean anyMatch(String requiredExpression, List<String> userExpressions) {
        for (String expression : userExpressions) {
            if (matches(requiredExpression, expression)) {
                return true;
            }
        }
        return false;
    }
} 