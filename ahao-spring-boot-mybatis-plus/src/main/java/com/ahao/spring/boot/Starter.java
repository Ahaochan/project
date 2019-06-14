package com.ahao.spring.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot方式启动类
 * @author Ahaochan
 */
@SpringBootApplication(scanBasePackages = "com.ahao")
@MapperScan("com.ahao.spring.boot.mybatis.plus.module.mapper")
public class Starter {

    private final static Logger logger = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Starter.class);
        app.run(args);
        logger.info(Starter.class.getSimpleName() + " is success!");
    }
}
