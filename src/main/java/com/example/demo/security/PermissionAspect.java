package com.example.demo.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import java.util.stream.Collectors;

import java.lang.reflect.Method;

/**
 * 处理@RequirePermission注解的切面
 */
@Aspect
@Component
public class PermissionAspect {

    private static final Logger logger = LoggerFactory.getLogger(PermissionAspect.class);

    @Autowired
    private CustomPermissionEvaluator permissionEvaluator;

    /**
     * 拦截使用@RequirePermission注解的方法调用，并检查当前用户是否拥有所需权限
     */
    @Around("@annotation(com.example.demo.security.RequirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        RequirePermission requiredPermission = method.getAnnotation(RequirePermission.class);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.error("Authentication is null or not authenticated");
            throw new AccessDeniedException("未经过身份验证");
        }
        
        String requiredPermissionStr = String.format("%s:%s:%s", 
            requiredPermission.resource(),
            requiredPermission.operation(),
            requiredPermission.field());
        
        logger.debug("Checking permission for method: {}", method.getName());
        logger.debug("Required permission: {}", requiredPermissionStr);
        logger.debug("User authorities: {}", 
            authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", ")));
        
        boolean hasPermission = permissionEvaluator.hasPermission(
                authentication,
                requiredPermission.resource(),
                requiredPermission.operation(),
                requiredPermission.field());
                
        logger.debug("Permission check result: {}", hasPermission);
        
        if (!hasPermission) {
            logger.warn("Permission denied for user: {} requiring permission: {}", 
                authentication.getName(), requiredPermissionStr);
            throw new AccessDeniedException(
                    "权限被拒绝: 需要 " + requiredPermissionStr);
        }
        
        return joinPoint.proceed();
    }
} 