package com.example.demo.dao;

import com.example.demo.entity.ZoneFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ZoneFunctionRepository extends JpaRepository<ZoneFunction, Integer> {
    // 根据分区类型查找
    List<ZoneFunction> findByFunctionId(Integer functionId);

    // 根据分区名称查找
    List<ZoneFunction> findByFunctionName(String functionName);


} 