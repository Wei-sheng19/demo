package com.example.demo.dao;

import com.example.demo.entity.MaintenanceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaintenanceInfoRepository extends JpaRepository<MaintenanceInfo, Long> {
    // 根据服务评级查找
    List<MaintenanceInfo> findByServiceRating(Integer rating);
    
    
    
    // 查找需要定期反馈的维护信息
    List<MaintenanceInfo> findByRegularFeedbackIsNotNull();

    // 查询房间的维护信息（包括审计信息）
    @Query("SELECT mi FROM MaintenanceInfo mi WHERE mi.room.roomId = :roomId")
    Optional<MaintenanceInfo> findByRoomId(@Param("roomId") Long roomId);
    
    // 查询审计信息
    @Query("SELECT mi.auditInfo FROM MaintenanceInfo mi WHERE mi.maintenanceInfoId = :maintenanceId")
    Optional<String> findAuditInfoById(@Param("maintenanceId") Long maintenanceId);
} 