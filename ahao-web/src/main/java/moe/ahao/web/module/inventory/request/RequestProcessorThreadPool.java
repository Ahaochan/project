package moe.ahao.web.module.inventory.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class RequestProcessorThreadPool implements InitializingBean, DisposableBean {
    private final static Logger logger = LoggerFactory.getLogger(RequestProcessorThreadPool.class);

    private int threadCount = 10;
    private int queueLength = 100;

    private ExecutorService threadPool;
    private List<BlockingQueue<Request>> queues;
    private ConcurrentMap<Long, Boolean> flagMap;

    public RequestProcessorThreadPool() {
        this.queues = new ArrayList<>();
        this.flagMap = new ConcurrentHashMap<>();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.threadPool = new ThreadPoolExecutor(threadCount, threadCount, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
            r -> {
                Thread thread = new Thread(r, "request-processor-thread" + r.hashCode());
                thread.setDaemon(true);
                return thread;
            });
        for (int i = 0; i < threadCount; i++) {
            BlockingQueue<Request> queue = new ArrayBlockingQueue<>(queueLength);
            this.queues.add(queue);
            threadPool.submit(new RequestProcessorThread(queue, flagMap));
        }
    }

    @Override
    public void destroy() {
        threadPool.shutdown();
        try {
            while (!threadPool.awaitTermination(1, TimeUnit.MINUTES)) {
            }
        } catch (InterruptedException e) {
            logger.error("关闭线程池失败", e);
        }
    }

    public RequestProcessorThreadPool setThreadCount(int threadCount) {
        this.threadCount = threadCount;
        return this;
    }

    public RequestProcessorThreadPool setBlockingQueueLength(int queueLength) {
        this.queueLength = queueLength;
        return this;
    }

    public int getQueuesCount() {
        return this.queues.size();
    }

    public BlockingQueue<Request> getQueue(int index) {
        return this.queues.get(index);
    }
}
