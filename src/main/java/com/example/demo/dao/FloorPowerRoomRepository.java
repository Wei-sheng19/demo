package com.example.demo.dao;

import com.example.demo.entity.FloorPowerRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FloorPowerRoomRepository extends JpaRepository<FloorPowerRoom, Long> {
    // 根据楼层ID查找配电间
    @Query("SELECT fpr FROM FloorPowerRoom fpr WHERE fpr.floor.id = :floorId")
    FloorPowerRoom findByFloorId(Long floorId);
    
    // 根据变电所ID查找所有配电间
    @Query("SELECT fpr FROM FloorPowerRoom fpr WHERE fpr.substation.id = :substationId")
    List<FloorPowerRoom> findBySubstationId(Long substationId);
    
    // 查找实际运行数据超过设计负荷的配电间
    @Query("SELECT fpr FROM FloorPowerRoom fpr WHERE fpr.actualOperationData > fpr.designLoad")
    List<FloorPowerRoom> findOverloadedRooms();
} 