package moe.ahao.spring.boot.async.config;

import com.alibaba.ttl.threadpool.TtlExecutors;
import moe.ahao.spring.boot.async.exception.AsyncExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Spring 异步任务 配置类
 *
 * 在异步任务方法加上 {@link org.springframework.scheduling.annotation.Async} 注解, 即可添加到自定义的线程池.
 * {@link org.springframework.scheduling.annotation.Async} 注解使用默认线程池 {@link #getAsyncExecutor()}, 也可以指定线程池.
 *
 * 异步任务可以返回 void 和 Future<T>
 */
@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        return executor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

    @Bean("asyncExecutor")
    @Primary
    public Executor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int core = Runtime.getRuntime().availableProcessors();
        // 设置核心线程数
        executor.setCorePoolSize(core);
        // 设置最大线程数
        executor.setMaxPoolSize(core * 2 + 1);
        // 设置队列容量
        executor.setQueueCapacity(20_000);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("springboot-executor-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return TtlExecutors.getTtlExecutor(executor);
    }

    @Bean("scheduleExecutor")
    public ScheduledExecutorService scheduleExecutor() {
        int core = Runtime.getRuntime().availableProcessors();
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(core);
        return TtlExecutors.getTtlScheduledExecutorService(threadPool);
    }

}
