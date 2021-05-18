package moe.ahao.web.module.inventory.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class RequestProcessorThreadPool {
    private final static Logger logger = LoggerFactory.getLogger(RequestProcessorThreadPool.class);

    private final int threadCount = 10;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);

    public void init() {
        for (int i = 0; i < threadCount; i++) {
            BlockingQueue<Request> queue = new ArrayBlockingQueue<>(100);
            RequestQueue.getInstance().addQueue(queue);
            threadPool.submit(new RequestProcessorThread(queue));
        }
    }

    public void destroy() {
        threadPool.shutdown();

        try {
            while (!threadPool.awaitTermination(1, TimeUnit.MINUTES)) {
            }
        } catch (InterruptedException e) {
            logger.error("关闭线程池失败", e);
        }
    }

    private static class Instance {
        private static final RequestProcessorThreadPool instance;
        static {
            instance = new RequestProcessorThreadPool();
        }
        private static RequestProcessorThreadPool getInstance() {
            return instance;
        }
    }
    public static RequestProcessorThreadPool getInstance() {
        return Instance.getInstance();
    }
}
