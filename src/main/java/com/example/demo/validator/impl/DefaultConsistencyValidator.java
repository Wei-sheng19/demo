package com.example.demo.validator.impl;

import com.example.demo.validator.ConsistencyValidator;
import com.example.demo.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DefaultConsistencyValidator implements ConsistencyValidator {
    
    @Autowired
    private BuildingRepository buildingRepository;
    
    @Autowired
    private FloorRepository floorRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Override
    public List<String> validateConsistency(String entityType, List<Map<String, String>> records) {
        List<String> errors = new ArrayList<>();
        
        // 1. 检查记录间的一致性
        validateInterRecordConsistency(entityType, records, errors);
        
        // 2. 检查每条记录的内部一致性
        for (Map<String, String> record : records) {
            errors.addAll(validateRecordConsistency(entityType, record));
        }
        
        return errors;
    }
    
    @Override
    public List<String> validateRecordConsistency(String entityType, Map<String, String> record) {
        List<String> errors = new ArrayList<>();
        
        switch (entityType.toLowerCase()) {
            case "building":
                validateBuildingConsistency(record, errors);
                break;
            case "floor":
                validateFloorConsistency(record, errors);
                break;
            case "room":
                validateRoomConsistency(record, errors);
                break;
            case "floorpowerroom":
                validateFloorPowerRoomConsistency(record, errors);
                break;
            case "constructioninfo":
                validateConstructionInfoConsistency(record, errors);
                break;
        }
        
        return errors;
    }
    
    private void validateInterRecordConsistency(String entityType, List<Map<String, String>> records, List<String> errors) {
        switch (entityType.toLowerCase()) {
            case "floor":
                validateFloorNumbersConsistency(records, errors);
                break;
            case "room":
                validateRoomNumbersConsistency(records, errors);
                break;
        }
    }
    
    private void validateBuildingConsistency(Map<String, String> record, List<String> errors) {
        // 1. 建设日期和竣工日期的逻辑关系
        try {
            String constructionDate = record.get("construction_date");
            String completionDate = record.get("completion_date");
            
            if (StringUtils.hasText(constructionDate) && StringUtils.hasText(completionDate)) {
                LocalDate construction = LocalDate.parse(constructionDate);
                LocalDate completion = LocalDate.parse(completionDate);
                
                if (completion.isBefore(construction)) {
                    errors.add("Completion date cannot be earlier than construction date");
                }
            }
        } catch (DateTimeParseException e) {
            errors.add("Invalid date format");
        }
        
        // 2. 建筑规模与层数的关系验证
        String floornumber = record.get("floornumber");
        String buildingScale = record.get("building_scale");
        if (StringUtils.hasText(floornumber) && StringUtils.hasText(buildingScale)) {
            try {
                int floors = Integer.parseInt(floornumber);
                double scale = Double.parseDouble(buildingScale);
                
                if (floors > 0 && scale <= 0) {
                    errors.add("Building scale must be positive when floor number is positive");
                }
            } catch (NumberFormatException e) {
                errors.add("Invalid number format for floor number or building scale");
            }
        }
        
        // 3. 地下空间相关属性的一致性
        String basementFloorCount = record.get("basement_floor_count");
        String basementArea = record.get("basement_area");
        if (StringUtils.hasText(basementFloorCount)) {
            try {
                int count = Integer.parseInt(basementFloorCount);
                if (count > 0 && !StringUtils.hasText(basementArea)) {
                    errors.add("Basement area is required when basement floor count is positive");
                }
            } catch (NumberFormatException e) {
                errors.add("Invalid number format for basement floor count");
            }
        }
    }
    
    private void validateFloorConsistency(Map<String, String> record, List<String> errors) {
        // 1. 楼层号的合法性验证
        String floorNumber = record.get("floor_number");
        String buildingId = record.get("building_id");
        
        if (StringUtils.hasText(floorNumber) && StringUtils.hasText(buildingId)) {
            try {
                int floor = Integer.parseInt(floorNumber);
                Long building = Long.valueOf(buildingId);
                
                // 检查楼层号是否在建筑物的合法范围内
                buildingRepository.findById(building).ifPresent(b -> {
                    if (floor > b.getFloornumber()) {
                        errors.add("Floor number exceeds building's total floor count");
                    }
                });
            } catch (NumberFormatException e) {
                errors.add("Invalid floor number format");
            }
        }
    }
    
    private void validateRoomConsistency(Map<String, String> record, List<String> errors) {
        // 1. 房间号格式验证
        String roomNumber = record.get("room_number");
        String floorId = record.get("floor_id");
        
        if (StringUtils.hasText(roomNumber) && StringUtils.hasText(floorId)) {
            // 检查房间号是否符合楼层编号规则
            floorRepository.findById(Long.valueOf(floorId)).ifPresent(floor -> {
                String floorNumber = String.valueOf(floor.getFloorNumber());
                if (!roomNumber.startsWith(floorNumber)) {
                    errors.add("Room number should start with floor number");
                }
            });
        }
    }
    
    private void validateFloorPowerRoomConsistency(Map<String, String> record, List<String> errors) {
        // 1. 配电间容量与变电所容量的关系验证
        String capacity = record.get("capacity");
        String substationId = record.get("substation_id");
        
        if (StringUtils.hasText(capacity) && StringUtils.hasText(substationId)) {
            try {
                double powerRoomCapacity = Double.parseDouble(capacity);
                if (powerRoomCapacity <= 0) {
                    errors.add("Power room capacity must be positive");
                }
            } catch (NumberFormatException e) {
                errors.add("Invalid capacity format");
            }
        }
    }
    
    private void validateConstructionInfoConsistency(Map<String, String> record, List<String> errors) {
        // 1. 施工日期和完工日期的逻辑关系
        try {
            String startDate = record.get("start_date");
            String endDate = record.get("end_date");
            
            if (StringUtils.hasText(startDate) && StringUtils.hasText(endDate)) {
                LocalDate start = LocalDate.parse(startDate);
                LocalDate end = LocalDate.parse(endDate);
                
                if (end.isBefore(start)) {
                    errors.add("End date cannot be earlier than start date");
                }
            }
        } catch (DateTimeParseException e) {
            errors.add("Invalid date format");
        }
    }
    
    private void validateFloorNumbersConsistency(List<Map<String, String>> records, List<String> errors) {
        // 检查同一建筑的楼层号是否连续且不重复
        Map<String, Set<Integer>> buildingFloors = new HashMap<>();
        
        for (Map<String, String> record : records) {
            String buildingId = record.get("building_id");
            String floorNumber = record.get("floor_number");
            
            if (StringUtils.hasText(buildingId) && StringUtils.hasText(floorNumber)) {
                try {
                    int floor = Integer.parseInt(floorNumber);
                    buildingFloors.computeIfAbsent(buildingId, k -> new HashSet<>()).add(floor);
                } catch (NumberFormatException e) {
                    errors.add("Invalid floor number format");
                }
            }
        }
        
        // 检查每个建筑的楼层号
        for (Map.Entry<String, Set<Integer>> entry : buildingFloors.entrySet()) {
            Set<Integer> floors = entry.getValue();
            int min = Collections.min(floors);
            int max = Collections.max(floors);
            
            // 检查是否连续
            for (int i = min; i <= max; i++) {
                if (!floors.contains(i)) {
                    errors.add("Building " + entry.getKey() + " has non-consecutive floor numbers");
                    break;
                }
            }
        }
    }
    
    private void validateRoomNumbersConsistency(List<Map<String, String>> records, List<String> errors) {
        // 检查同一楼层的房间号是否唯一
        Map<String, Set<String>> floorRooms = new HashMap<>();
        
        for (Map<String, String> record : records) {
            String floorId = record.get("floor_id");
            String roomNumber = record.get("room_number");
            
            if (StringUtils.hasText(floorId) && StringUtils.hasText(roomNumber)) {
                if (!floorRooms.computeIfAbsent(floorId, k -> new HashSet<>()).add(roomNumber)) {
                    errors.add("Duplicate room number " + roomNumber + " in floor " + floorId);
                }
            }
        }
    }
} 