package com.example.demo.dao;

import com.example.demo.entity.ConstructionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Date;

@Repository
public interface ConstructionInfoRepository extends JpaRepository<ConstructionInfo, Long> {
    // 根据项目名称查找
    ConstructionInfo findByProjectName(String projectName);
    
    // 根据审核信息模糊查询
    List<ConstructionInfo> findByAuditInfoContaining(String auditInfo);
    
    // 根据审核状态查询
    List<ConstructionInfo> findByAuditStatus(ConstructionInfo.AuditStatus status);
    
    // 根据时间范围查询
    List<ConstructionInfo> findByCreatedAtBetween(Date startDate, Date endDate);
    
    // 查询某个建筑的所有施工信息
    @Query("SELECT c FROM ConstructionInfo c WHERE c.room.floor.building.id = :buildingId AND c.isDeleted = false")
    List<ConstructionInfo> findByBuildingId(@Param("buildingId") Long buildingId);
    
    // 查询某个楼层的所有施工信息
    @Query("SELECT c FROM ConstructionInfo c WHERE c.room.floor.id = :floorId AND c.isDeleted = false")
    List<ConstructionInfo> findByFloorId(@Param("floorId") Long floorId);
    
    // 软删除查询
    @Query("SELECT c FROM ConstructionInfo c WHERE c.isDeleted = false")
    List<ConstructionInfo> findAllActive();
} 