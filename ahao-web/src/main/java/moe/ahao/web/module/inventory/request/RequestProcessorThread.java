package moe.ahao.web.module.inventory.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class RequestProcessorThread implements Callable<Boolean> {
    private final static Logger logger = LoggerFactory.getLogger(RequestProcessorThread.class);

    private BlockingQueue<Request> queue;
    private ConcurrentMap<Long, Boolean> flagMap;
    private volatile boolean isRunning;
    public RequestProcessorThread(BlockingQueue<Request> queue, ConcurrentMap<Long, Boolean> flagMap) {
        this.queue = queue;
        this.flagMap = flagMap;
        this.isRunning = true;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            while (isRunning) {
                // 1. 阻塞队列获取请求
                Request request = queue.poll(1, TimeUnit.SECONDS);
                if(request == null) {
                    continue;
                }

                Long productId = request.getProductId();
                logger.info("线程处理请求, id:{}", productId);

                // 2. 请求去重
                if(request instanceof ProductInventoryDBUpdateRequest) {
                    // 2.1. 如果是更新请求就设置为true
                    flagMap.put(productId, Boolean.TRUE);
                } else if(request instanceof ProductInventoryCacheReloadRequest){
                    // 2.2. 如果是强制刷新请求就跳过校验
                    boolean forceRefresh = ((ProductInventoryCacheReloadRequest) request).isForceRefresh();
                    if(!forceRefresh) {
                        Boolean flag = flagMap.get(productId);
                        if (flag == null) {
                            // 如果标识为空, 说明没有写请求、读请求, 标记为false, 去处理读请求
                            flagMap.put(productId, false);
                        }

                        if(flag != null && flag) {
                            // 如果标识不为空, 且为true, 说明之前就有一个写请求了, 标记为false, 去处理读请求
                            flagMap.put(productId, false);
                        }

                        if (flag != null && !flag) {
                            // 如果标识不为空, 且为false, 说明之前就有一个写请求、读请求了, 就不再去执行读请求了
                            continue;
                        }
                    }
                }


                request.process();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void shutdown() {
        this.isRunning = false;
    }
}
