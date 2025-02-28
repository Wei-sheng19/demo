package com.example.demo.dao;

import com.example.demo.entity.Building;
import com.example.demo.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    // 根据楼栋编号查找
    Building findByBuildingNumber(String buildingNumber);
    
    // 根据名称查找
    Building findByName(String name);
    
    // 查找所有文物保护建筑
    List<Building> findByIsHeritageSiteTrue();
    
    // 根据校区ID查找建筑
    @Query("SELECT b FROM Building b WHERE b.campus.id = :campusId")
    List<Building> findByCampusId(Long campusId);
    
    // 根据用途查找建筑
    List<Building> findByPurpose(String purpose);

    @Query("""
    SELECT DISTINCT r FROM Room r 
    JOIN FETCH r.floor f 
    JOIN r.roomFunctions rf 
    JOIN rf.zoneFunction zf 
    WHERE f.building.id = :buildingId 
    AND zf.functionId = :functionId
""")
    List<Room> findRoomsByBuildingIdAndFunctionId(
            @Param("buildingId") Long buildingId,
            @Param("functionId") Integer functionId
    );
} 