package moe.ahao.spring.cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot方式启动类
 * @author Ahaochan
 */
@SpringBootApplication(scanBasePackages = "moe.ahao")
public class Starter {
    private static final Logger logger = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) {
        try {
            SpringApplication app = new SpringApplication(Starter.class);
            app.run(args);
            logger.info("{}启动成功!", Starter.class.getSimpleName());
        } catch (Exception e) {
            logger.error("{}启动失败!", Starter.class.getSimpleName(), e);
        }
    }
}
