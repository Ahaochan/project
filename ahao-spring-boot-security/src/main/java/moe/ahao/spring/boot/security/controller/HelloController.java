package moe.ahao.spring.boot.security.controller;

import com.ahao.util.commons.lang.time.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    public String hello() {
        return "hello" + DateHelper.getNow(DateHelper.yyyyMMdd_hhmmssSSS);
    }

    @GetMapping("/api/hello")
    public String api() {
        return "api: hello" + DateHelper.getNow(DateHelper.yyyyMMdd_hhmmssSSS);
    }
}
