package com.example.demo.service.impl;

import com.example.demo.dao.CampusRepository;
import com.example.demo.dto.campus.CampusDTO;
import com.example.demo.entity.Campus;
import com.example.demo.service.CampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CampusServiceImpl implements CampusService {

    @Autowired
    private CampusRepository campusRepository;

    @Override
    @Transactional(readOnly = true)
    public CampusDTO getCampusInfo(Long campusId) {
        Optional<Campus> optionalCampus = campusRepository.findById(campusId);
        Campus campus = optionalCampus.orElseThrow(() -> new IllegalArgumentException("Campus not found with id: " + campusId));
        return CampusDTO.fromCampus(campus);
    }

    @Override
    @Transactional(readOnly = true)
    public CampusDTO getCampusInfoByName(String campusName) {
        Optional<Campus> optionalCampus = Optional.ofNullable(campusRepository.findByName(campusName));
        Campus campus = optionalCampus.orElseThrow(() -> new IllegalArgumentException("Campus not found with name: " + campusName));
        return CampusDTO.fromCampus(campus);
    }


}