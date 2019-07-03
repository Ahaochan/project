package com.ahao.spring.cloud.config.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Value("${info.description}")
    private String description;

    @GetMapping("/test")
    public String test() {
        return description;
    }
}
