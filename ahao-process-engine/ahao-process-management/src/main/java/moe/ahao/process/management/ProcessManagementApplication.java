package moe.ahao.process.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot方式启动类
 *
 * @author Ahaochan
 */
@SpringBootApplication
public class ProcessManagementApplication {
    private static final Logger logger = LoggerFactory.getLogger(ProcessManagementApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication app = new SpringApplication(ProcessManagementApplication.class);
            app.run(args);
            logger.info("{}启动成功!", ProcessManagementApplication.class.getSimpleName());
        } catch (Exception e) {
            logger.error("{}启动失败!", ProcessManagementApplication.class.getSimpleName(), e);
        }
    }
}
