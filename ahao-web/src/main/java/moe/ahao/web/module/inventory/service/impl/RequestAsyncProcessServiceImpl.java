package moe.ahao.web.module.inventory.service.impl;

import moe.ahao.web.module.inventory.request.Request;
import moe.ahao.web.module.inventory.request.RequestProcessorThreadPool;
import moe.ahao.web.module.inventory.service.RequestAsyncProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;

@Service
public class RequestAsyncProcessServiceImpl implements RequestAsyncProcessService {
    private final static Logger logger = LoggerFactory.getLogger(RequestAsyncProcessServiceImpl.class);

    @Autowired
    private RequestProcessorThreadPool threadPool;

    @Override
    public void process(Request request) {
        Long productId = request.getProductId();
        BlockingQueue<Request> queue = this.getRoutingQueue(productId);
        queue.add(request);
    }

    private BlockingQueue<Request> getRoutingQueue(Long id) {
        int h;
        int hash = (id == null) ? 0 : (h = id.hashCode()) ^ (h >>> 16);
        int index = (threadPool.getQueuesCount() - 1) & hash;

        logger.info("路由内存队列, id:{}, index:{}", id, index);
        return threadPool.getQueue(index);
    }
}
