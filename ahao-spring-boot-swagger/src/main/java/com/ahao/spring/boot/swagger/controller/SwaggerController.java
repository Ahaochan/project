package com.ahao.spring.boot.swagger.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ConditionalOnProperty(name = "swagger.open", havingValue = "true")
//@Api(hidden = true) // 无效
@ApiIgnore
public class SwaggerController {

    @GetMapping("/swagger")
    public String swagger() {
        return "redirect:/swagger-ui.html";
    }
}
