package com.ahao.invoice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Ahaochan on 2017/9/8.
 */
@Controller
public class TestController {
    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
