package com.example.demo.strategy;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

public interface ImportStrategy {
    /**
     * 解析上传的文件
     * @param file 上传的文件
     * @return 解析后的数据列表，每行数据用Map表示
     */
    List<Map<String, String>> parseFile(MultipartFile file) throws Exception;
    
    /**
     * 验证文件格式
     * @param file 上传的文件
     * @return 是否为有效的文件格式
     */
    boolean validateFileFormat(MultipartFile file);
} 