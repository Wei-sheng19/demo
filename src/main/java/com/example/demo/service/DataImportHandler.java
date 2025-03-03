package com.example.demo.service;

import java.util.Map;

public interface DataImportHandler<T> {
    /**
     * 将CSV数据转换为实体对象
     * @param data CSV数据
     * @return 实体对象
     */
    T convertToEntity(Map<String, String> data);
    
    /**
     * 保存或更新实体对象
     * @param entity 实体对象
     * @param updateExisting 是否更新已存在的数据
     * @return 保存后的实体对象
     */
    T saveOrUpdate(T entity, boolean updateExisting);
    
    /**
     * 获取处理器支持的实体类型
     * @return 实体类型名称
     */
    String getEntityType();
} 