package com.ahao.web;

import com.ahao.spring.bean.PackageBeanNameGenerator;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * SpringBoot方式启动类
 * @author Ahaochan
 */
@SpringBootApplication(scanBasePackages = "com.ahao", exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.ahao")
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class AhaoApplication {

    private final static Logger logger = LoggerFactory.getLogger(AhaoApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AhaoApplication.class);
        app.setBeanNameGenerator(new PackageBeanNameGenerator());
        app.run(args);
        logger.info(AhaoApplication.class.getSimpleName() + " is success!");
    }
}
