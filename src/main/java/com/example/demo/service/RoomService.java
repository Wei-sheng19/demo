package com.example.demo.service;

import com.example.demo.dto.*;

import java.util.List;

public interface RoomService {
    RoomDTO getRoomBasicInfo(Long roomId);

    List<RoomFunctionDTO> getCurrentFunction(Long roomId);

    RoomDetailsDTO getRoomDetails(Long roomId);


}