package moe.ahao.spring.boot.dependency;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class TestConfig {
    @Bean("asyncExecutor")
    public Executor executor() {
        int coreNum = Runtime.getRuntime().availableProcessors();
        return Executors.newFixedThreadPool(coreNum);
    }
}
