package com.example.demo.dao;
import java.util.List;

import com.example.demo.entity.RoomDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDetailsRepository extends JpaRepository<RoomDetails, Long> {
    // 根据房间ID查找详细信息
    RoomDetails findByRoomRoomId(Long roomId);
    
    // 根据用途查找房间详细信息
    List<RoomDetails> findByDesignedPurpose(RoomDetails.Purpose purpose);
    
    // 根据面积范围查找
    List<RoomDetails> findByAreaBetween(Double minArea, Double maxArea);
} 