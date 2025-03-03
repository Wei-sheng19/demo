package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.*;

@Configuration
public class EntityDependencyConfig {
    
    @Bean
    public Map<String, Set<String>> entityDependencies() {
        Map<String, Set<String>> dependencies = new HashMap<>();
        
        // 定义每个实体的依赖
        dependencies.put("campus", new HashSet<>());  // 校区没有依赖
        dependencies.put("substation", new HashSet<>());  // 变电所没有依赖
        dependencies.put("building", new HashSet<>(Arrays.asList("campus"))); // 建筑依赖校区，变电所是可选的
        dependencies.put("floor", new HashSet<>(Arrays.asList("building")));  // 楼层依赖建筑
        dependencies.put("floorPowerRoom", new HashSet<>(Arrays.asList("floor", "substation")));  // 配电间依赖楼层和变电所
        dependencies.put("room", new HashSet<>(Arrays.asList("floor")));  // 房间依赖楼层
        dependencies.put("roomDetails", new HashSet<>(Arrays.asList("room")));  // 房间详情依赖房间
        dependencies.put("zoneFunction", new HashSet<>());  // 区域功能没有依赖
        dependencies.put("roomFunction", new HashSet<>(Arrays.asList("room", "zoneFunction")));  // 房间功能依赖房间和区域功能
        dependencies.put("materialEquipment", new HashSet<>(Arrays.asList("room")));  // 材料设备依赖房间
        dependencies.put("constructionInfo", new HashSet<>(Arrays.asList("room")));  // 建设信息依赖房间
        dependencies.put("maintenanceInfo", new HashSet<>(Arrays.asList("room")));  // 维护信息依赖房间
        
        return dependencies;
    }
    
    // ... 其他方法保持不变 ...
} 