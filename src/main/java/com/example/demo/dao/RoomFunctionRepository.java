package com.example.demo.dao;
import java.util.List;

import com.example.demo.entity.RoomFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomFunctionRepository extends JpaRepository<RoomFunction, Long> {
    // 根据房间ID查找房间功能
    List<RoomFunction> findByRoomRoomId(Long roomId);

    // 根据功能ID查找房间功能
    List<RoomFunction> findByZoneFunctionFunctionId(Integer functionId);

} 