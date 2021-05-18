package moe.ahao.web.module.inventory.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class RequestProcessorThread implements Callable<Boolean> {
    private final static Logger logger = LoggerFactory.getLogger(RequestProcessorThread.class);

    private BlockingQueue<Request> queue;
    public RequestProcessorThread(BlockingQueue<Request> queue) {
        this.queue = queue;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            while (true) {
                Request request = queue.take();
                Long productId = request.getProductId();
                logger.info("线程处理请求, id:{}", productId);

                RequestQueue requestQueue = RequestQueue.getInstance();
                Map<Long, Boolean> flagMap = requestQueue.getFlagMap();
                if(request instanceof ProductInventoryDBUpdateRequest) {
                    flagMap.put(productId, true);
                } else if(request instanceof ProductInventoryCacheReloadRequest){
                    boolean forceRefresh = ((ProductInventoryCacheReloadRequest) request).isForceRefresh();
                    if(!forceRefresh) {
                        Boolean flag = flagMap.get(productId);
                        if (flag == null) {
                            flagMap.put(productId, false);
                        }

                        if (flag != null && flag) {
                            flagMap.put(productId, false);
                        }

                        if (flag != null && !flag) {
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
}
