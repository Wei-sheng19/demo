package com.example.demo.dao;

import com.example.demo.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FloorRepository extends JpaRepository<Floor, Long> {
    // 根据楼层编号查找
    Floor findByFloorNumber(String floorNumber);
    
    // 根据建筑ID查找所有楼层
    @Query("SELECT f FROM Floor f WHERE f.building.id = :buildingId")
    List<Floor> findByBuildingId(Long buildingId);
    
    // 查找特定建筑中大于指定荷载的楼层
    @Query("SELECT f FROM Floor f WHERE f.building.id = :buildingId AND f.load > :minLoad")
    List<Floor> findByBuildingIdAndLoadGreaterThan(Long buildingId, Double minLoad);
} 