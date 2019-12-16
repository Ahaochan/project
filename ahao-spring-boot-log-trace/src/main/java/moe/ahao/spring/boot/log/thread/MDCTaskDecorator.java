package moe.ahao.spring.boot.log.thread;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * 使用 {@link org.springframework.scheduling.annotation.Async} 实现的异步任务下的 MDC 传递方案
 * 使用方法: {@link org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor#setTaskDecorator(TaskDecorator)} 方法
 * <p>
 * ThreadPoolTaskExecutor 并不是一个真正的线程池, 其内部有一个 ThreadPoolExecutor 属性, 是在 afterPropertiesSet() 方法内通过 new 进行初始化的.
 * 当执行 execute 或者 submit 方法的时候, 因为内部的 ThreadPoolExecutor 属性不在 Spring 容器的管理范围内, 所以 AOP 失效.
 *
 * @see MDCThreadPoolExecutorAspect
 */
public class MDCTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable oldRunnable) {
        // 1. 获取父线程的 MDC
        Map<String, String> context = MDC.getCopyOfContextMap();
        if (MapUtils.isEmpty(context)) {
            return oldRunnable;
        }

        // 2. 将父线程的 MDC 传给子线程
        Runnable newRunnable = () -> {
            MDC.setContextMap(context);
            try {
                oldRunnable.run();
            } finally {
                MDC.clear(); // 清空子线程的 MDC
            }
        };
        return newRunnable;
    }
}
