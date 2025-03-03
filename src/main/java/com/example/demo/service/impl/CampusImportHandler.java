package com.example.demo.service.impl;

import com.example.demo.dao.CampusRepository;
import com.example.demo.entity.Campus;
import com.example.demo.service.DataImportHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CampusImportHandler implements DataImportHandler<Campus> {
    
    @Autowired
    private CampusRepository campusRepository;
    
    @Override
    public Campus convertToEntity(Map<String, String> data) {
        Campus campus = new Campus();
        campus.setName(data.get("name"));
        campus.setLocation(data.get("location"));
        campus.setFloorPlan(data.get("floorPlan"));
        campus.setThreeDModel(data.get("threeDModel"));
        return campus;
    }
    
    @Override
    public Campus saveOrUpdate(Campus entity, boolean updateExisting) {
        if (updateExisting) {
            Campus existingCampus = campusRepository.findByName(entity.getName());
            if (existingCampus != null) {
                existingCampus.setLocation(entity.getLocation());
                existingCampus.setFloorPlan(entity.getFloorPlan());
                existingCampus.setThreeDModel(entity.getThreeDModel());
                return campusRepository.save(existingCampus);
            }
        }
        return campusRepository.save(entity);
    }
    
    @Override
    public String getEntityType() {
        return "campus";
    }
} 