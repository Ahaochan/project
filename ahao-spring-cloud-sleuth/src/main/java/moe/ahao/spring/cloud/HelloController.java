package moe.ahao.spring.cloud;

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
        String msg = "hello " + DateHelper.getNow("yyyy-MM-dd hh:mm:ss");
        logger.error(msg);
        return msg;
    }
}
