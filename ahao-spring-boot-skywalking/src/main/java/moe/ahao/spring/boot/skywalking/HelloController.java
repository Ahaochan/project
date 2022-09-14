package moe.ahao.spring.boot.skywalking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class HelloController {
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    public String hello() {
        String msg = "now: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        logger.info("hello请求:{}", msg);
        return msg;
    }

    @GetMapping("/exception")
    public String exception() {
        logger.info("exception请求");
        throw new RuntimeException("exception");
    }
}
