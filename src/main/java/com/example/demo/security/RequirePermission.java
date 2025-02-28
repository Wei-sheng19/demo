package com.example.demo.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记需要特定权限的方法的注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    /**
     * 要检查权限的资源
     */
    String resource();
    
    /**
     * 要检查权限的操作
     */
    String operation();
    
    /**
     * 要检查权限的字段，默认为通配符
     */
    String field() default "*";
} 