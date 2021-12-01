package moe.ahao.spring.boot.skywalking;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "now: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    @GetMapping("/exception")
    public String exception() {
        throw new RuntimeException("exception");
    }
}
