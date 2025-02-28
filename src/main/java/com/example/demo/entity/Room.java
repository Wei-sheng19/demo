package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;  // 房间ID


    @Column(name = "room_number", nullable = false, length = 50)
    private String roomNumber;  // 房间编号

    @Column(name = "room_name", length = 100)
    private String roomName;  // 房间名称

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private Floor floor;

    @OneToOne(mappedBy = "room", cascade = CascadeType.ALL, optional = false)
    private RoomDetails roomDetails;  // 房间详细信息


    @OneToOne
    @JoinColumn(name = "construction_info_id", nullable = true)
    private ConstructionInfo constructionInfo;  // 建设信息

    @OneToOne
    @JoinColumn(name = "maintenance_info_id", nullable = true)
    private MaintenanceInfo maintenanceInfo;  // 运维信息


    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaterialEquipment> materialEquipments;  // 材料设备列表

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<RoomFunction> roomFunctions;  // 房间与分区功能的关联关系
}
