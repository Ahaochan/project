package com.ruyuan.eshop;

import moe.ahao.tend.consistency.core.annotation.EnableTendConsistencyTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zhonghuashishan
 **/
@EnableTendConsistencyTask
@EnableScheduling
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
