package com.example.demo.dao;

import com.example.demo.entity.Substation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubstationRepository extends JpaRepository<Substation, Long> {
    // 根据名称查找
    Substation findByName(String name);
    
    // 根据位置查找
    Substation findByLocation(String location);
    
    // 查找负载率高于指定值的变电所
    List<Substation> findByTransformerLoadRateGreaterThan(Double rate);
    
    // 获取变电所及其关联的建筑
    @Query("SELECT s FROM Substation s LEFT JOIN FETCH s.buildings WHERE s.id = :id")
    Substation findWithBuildings(Long id);

    @Query("SELECT s FROM Substation s " +
           "LEFT JOIN FETCH s.buildings " +
           "LEFT JOIN FETCH s.floorPowerRooms " +
           "WHERE s.id = :id")
    Optional<Substation> findByIdWithDetails(Long id);
} 