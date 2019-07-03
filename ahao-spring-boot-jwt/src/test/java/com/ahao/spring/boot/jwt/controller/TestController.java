package com.ahao.spring.boot.jwt.controller;

import com.ahao.spring.boot.jwt.annotation.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Jwt
    @GetMapping("/test")
    public String hello(String msg) {
        return "接收:" + msg;
    }
}
