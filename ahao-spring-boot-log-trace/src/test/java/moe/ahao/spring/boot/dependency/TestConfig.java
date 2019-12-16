package moe.ahao.spring.boot.dependency;

import moe.ahao.spring.boot.log.thread.MDCTaskDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync(proxyTargetClass = true)
public class TestConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        return executor();
    }

    @Bean("asyncExecutor")
    public Executor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("taskExecutor-");
        executor.setTaskDecorator(new MDCTaskDecorator());
        return executor;
        // return Executors.newFixedThreadPool(2);
    }
}
