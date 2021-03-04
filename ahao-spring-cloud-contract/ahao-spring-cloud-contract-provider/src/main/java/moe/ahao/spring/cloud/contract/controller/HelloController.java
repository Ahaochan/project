package moe.ahao.spring.cloud.contract.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    private String hello(@RequestParam String name) {
        return "hello" + name;
    }
}
