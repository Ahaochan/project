package moe.ahao.web.module.inventory.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class RequestQueue {
    private List<BlockingQueue<Request>> queues = new ArrayList<>();
    private Map<Long, Boolean> flagMap = new ConcurrentHashMap<>();

    public RequestQueue addQueue(BlockingQueue<Request> queue) {
        queues.add(queue);
        return this;
    }

    public BlockingQueue<Request> getQueue(int index) {
        return queues.get(index);
    }

    public int getQueueSize() {
        return queues.size();
    }

    public Map<Long, Boolean> getFlagMap() {
        return flagMap;
    }

    private static class Instance {
        private static final RequestQueue instance;
        static {
            instance = new RequestQueue();
        }
        private static RequestQueue getInstance() {
            return instance;
        }
    }
    public static RequestQueue getInstance() {
        return RequestQueue.Instance.getInstance();
    }
}
