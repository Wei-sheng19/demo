package com.example.demo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImportRequest {
    private String entityType; // 要导入的实体类型
    private MultipartFile file; // 上传的文件
    private boolean updateExisting; // 是否更新已存在的数据
} 