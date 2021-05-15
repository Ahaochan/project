package com.ahao.web.module.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@RestController
@RequestMapping("/async")
public class AsyncController {
    private final static Logger logger = LoggerFactory.getLogger(AsyncController.class);
    private final static BlockingQueue<DeferredResult<String>> queue = new LinkedBlockingDeque<>();

    static {
        new Thread(() -> {
            for (;;) {
                try {
                    DeferredResult<String> r = getDeferredResult();
                    logger.info("开始异步处理队列内的请求");
                    Thread.sleep(1000L);
                    logger.info("结束异步处理队列内的请求");
                    r.setResult("测试:" + System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @GetMapping("/test")
    public DeferredResult<String> async() throws InterruptedException {
        DeferredResult<String> deferredResult = new DeferredResult<>(3000L, "failure");
        queue.put(deferredResult);
        return deferredResult;
    }

    private static DeferredResult<String> getDeferredResult() {
        for (;;) {
            try {
                return queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
