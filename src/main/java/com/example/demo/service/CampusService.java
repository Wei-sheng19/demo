package com.example.demo.service;

import com.example.demo.dto.CampusDTO;

public interface CampusService {
    // 获取校园基本信息
    CampusDTO getCampusInfo(Long campusId);
    // 根据名称获取校园基本信息
    CampusDTO getCampusInfoByName(String campusName);

} 