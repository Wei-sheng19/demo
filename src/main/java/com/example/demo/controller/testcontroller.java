package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testcontroller {
    // 测试接口可以不需要权限控制或使用permitAll
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
