package com.example.demo.dao;
import java.util.List;

import com.example.demo.entity.MaterialEquipment;
import com.example.demo.dto.MaterialEquipmentWithLocationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialEquipmentRepository extends JpaRepository<MaterialEquipment, Integer> {

    // 根据设备类别查找
    List<MaterialEquipment> findByCategory(String category);

    // 根据材料/设备名称查找
    List<MaterialEquipment> findByMaterialName(String materialName);

    // 修改查找特定楼层的特定材料/设备的方法，添加更多位置信息
    @Query("""
        SELECT new com.example.demo.dto.MaterialEquipmentWithLocationDTO(
            me.materialEquipmentId, me.category, me.materialName, me.quantityOrArea,
            me.technicalRequirements, me.constructionDepartment, me.maintenanceDepartment,
            me.vendorInfo, me.productCost, me.installationTime, me.lifecycleWarningTime,
            me.replacementPeriod, me.vendorName, me.vendorContact, me.warrantyPeriod,
            me.productLifespan, me.samplePhotos,
            r.roomNumber, r.roomName, f.floorNumber)
        FROM MaterialEquipment me 
        JOIN me.room r 
        JOIN r.floor f 
        WHERE f.id = :floorId 
        AND me.materialName = :materialName
    """)
    List<MaterialEquipmentWithLocationDTO> findDetailsByFloorIdAndMaterialName(
        @Param("floorId") Long floorId, 
        @Param("materialName") String materialName
    );

    // 查找特定楼层的所有不重复材料/设备
    @Query("""
    SELECT DISTINCT new com.example.demo.dto.MaterialEquipmentWithLocationDTO(
        me.materialEquipmentId, me.category, me.materialName, me.quantityOrArea,
        me.technicalRequirements, me.constructionDepartment, me.maintenanceDepartment,
        me.vendorInfo, me.productCost, me.installationTime, me.lifecycleWarningTime,
        me.replacementPeriod, me.vendorName, me.vendorContact, me.warrantyPeriod,
        me.productLifespan, me.samplePhotos,
        r.roomNumber, r.roomName, f.floorNumber)
    FROM MaterialEquipment me 
    JOIN me.room r 
    JOIN r.floor f 
    WHERE f.id = :floorId
    GROUP BY 
        me.materialEquipmentId, 
        me.category, 
        me.materialName, 
        me.quantityOrArea,
        me.technicalRequirements, 
        me.constructionDepartment, 
        me.maintenanceDepartment,
        me.vendorInfo, 
        me.productCost, 
        me.installationTime, 
        me.lifecycleWarningTime,
        me.replacementPeriod, 
        me.vendorName, 
        me.vendorContact, 
        me.warrantyPeriod,
        me.productLifespan, 
        me.samplePhotos,
        r.roomNumber, 
        r.roomName, 
        f.floorNumber
""")
    List<MaterialEquipmentWithLocationDTO> findDistinctDetailsByFloorId(@Param("floorId") Long floorId);

    // 查找特定建筑的所有不重复材料/设备
    @Query("""
    SELECT DISTINCT new com.example.demo.dto.MaterialEquipmentWithLocationDTO(
        me.materialEquipmentId, me.category, me.materialName, me.quantityOrArea,
        me.technicalRequirements, me.constructionDepartment, me.maintenanceDepartment,
        me.vendorInfo, me.productCost, me.installationTime, me.lifecycleWarningTime,
        me.replacementPeriod, me.vendorName, me.vendorContact, me.warrantyPeriod,
        me.productLifespan, me.samplePhotos,
        r.roomNumber, r.roomName, f.floorNumber)
    FROM MaterialEquipment me 
    JOIN me.room r 
    JOIN r.floor f 
    JOIN f.building b 
    WHERE b.id = :buildingId
    GROUP BY 
        me.materialEquipmentId, 
        me.category, 
        me.materialName, 
        me.quantityOrArea,
        me.technicalRequirements, 
        me.constructionDepartment, 
        me.maintenanceDepartment,
        me.vendorInfo, 
        me.productCost, 
        me.installationTime, 
        me.lifecycleWarningTime,
        me.replacementPeriod, 
        me.vendorName, 
        me.vendorContact, 
        me.warrantyPeriod,
        me.productLifespan, 
        me.samplePhotos,
        r.roomNumber, 
        r.roomName, 
        f.floorNumber
""")
    List<MaterialEquipmentWithLocationDTO> findDistinctDetailsByBuildingId(@Param("buildingId") Long buildingId);

} 