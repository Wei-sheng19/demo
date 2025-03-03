package com.example.demo.dao;

import com.example.demo.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    // 根据房间编号查找
    Room findByRoomNumber(String roomNumber);
    
    // 根据楼层ID查找房间
    @Query("SELECT r FROM Room r WHERE r.floor.id = :floorId")
    List<Room> findByFloorId(@Param("floorId") Long floorId);
    
    // 根据房间名称查找房间
    List<Room> findByRoomName(String roomName);

    @Query("SELECT CASE WHEN COUNT(rd) > 0 THEN true ELSE false END FROM RoomDetails rd WHERE rd.room.id = :roomId")
    boolean existsByRoomDetailsRoomId(@Param("roomId") Long roomId);
} 