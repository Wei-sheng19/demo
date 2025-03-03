package com.example.demo.service;

import com.example.demo.dto.ImportRequest;
import com.example.demo.dto.ImportResponse;
import java.util.List;

/**
 * 数据导入服务接口
 */
public interface ImportService {
    /**
     * 处理单个实体类型的数据导入请求
     * @param request 导入请求
     * @return 导入响应
     */
    ImportResponse importData(ImportRequest request);
    
    /**
     * 处理多个实体类型的批量导入请求
     * @param requests 导入请求列表
     * @return 导入响应列表
     */
    List<ImportResponse> importDataBatch(List<ImportRequest> requests);
    
    /**
     * 获取推荐的导入顺序
     * @return 实体类型的导入顺序列表
     */
    List<String> getRecommendedImportOrder();
} 