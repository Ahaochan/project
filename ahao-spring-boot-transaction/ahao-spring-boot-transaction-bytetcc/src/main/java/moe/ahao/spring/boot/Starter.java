package moe.ahao.spring.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * SpringBoot方式启动类
 * @author Ahaochan
 */
@SpringBootApplication(scanBasePackages = "moe.ahao", exclude = {
    DataSourceTransactionManagerAutoConfiguration.class, MongoAutoConfiguration.class})
public class Starter {

    private final static Logger logger = LoggerFactory.getLogger(Starter.class);

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
