package moe.ahao.spring.cloud.module.hello.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import moe.ahao.spring.cloud.module.hello.controller.HelloController;
import moe.ahao.util.commons.lang.RandomHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Service
public class HelloService {
    public static final Logger logger = LoggerFactory.getLogger(HelloService.class);

    @HystrixCommand(fallbackMethod = "helloHystrix", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
    })
    public String hello() {
        try {
            Thread.sleep(RandomHelper.getInt(2000));
        } catch (InterruptedException ignored) {}
        return "hello";
    }

    @HystrixCommand(fallbackMethod = "helloHystrix", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")
    },
        threadPoolKey = "threadPoolKey",
        threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "10"),
            @HystrixProperty(name = "maxQueueSize", value = "2000"),
            @HystrixProperty(name = "queueSizeRejectionThreshold", value = "30"),
        })
    public String thread() {
        logger.info("thread 线程池隔离");
        assertTransmit();
        return "thread";
    }

    @HystrixCommand(fallbackMethod = "helloHystrix", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
        @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "10")
    })
    public String semaphore() {
        logger.info("semaphore 信号量隔离");
        assertTransmit();
        return "semaphore";
    }

    public String helloHystrix(Throwable e) {
        logger.error("hystrix 调用失败", e);
        return "helloHystrix";
    }


    private void assertTransmit() {
        String value1 = RequestContextHolder.currentRequestAttributes().getAttribute(HelloController.KEY, RequestAttributes.SCOPE_REQUEST).toString();
        logger.info("RequestContextHolder 获取值:" + value1);
        String value2 = HelloController.threadLocal.get();
        logger.info("ThreadLocal 获取值:" + value2);
    }

}
