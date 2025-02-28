package com.example.demo.dao;

import com.example.demo.entity.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampusRepository extends JpaRepository<Campus, Long> {
    // 根据名称查找校区
    Campus findByName(String name);
    
    // 根据位置查找校区
    Campus findByLocation(String location);
} 