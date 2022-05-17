package moe.ahao.spring.boot.sentinel;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class SentinelController {
    @Autowired
    private SentinelService sentinelService;

    @GetMapping("/controller")
    public String controller() {
        return DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    @GetMapping("/sentinelResource")
    public String sentinelResource(@RequestParam(defaultValue = "") String prefix) {
        return sentinelService.test(prefix);
    }
}
