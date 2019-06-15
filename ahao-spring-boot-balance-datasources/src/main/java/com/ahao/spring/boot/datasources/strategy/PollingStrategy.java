package com.ahao.spring.boot.datasources.strategy;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 负载均衡算法, 轮询实现, 可接入 Redis
 */
public class PollingStrategy implements LoadBalanceStrategy{
    private Map<String, AtomicInteger> index = new ConcurrentHashMap<>();
    @Override
    public DataSource determineDataSource(String groupName, List<DataSource> dataSources) {
        AtomicInteger poll = index.get(groupName);
        if(poll == null) {
            poll = new AtomicInteger(0);
            index.put(groupName, poll);
        }
        int i = poll.getAndIncrement();
        return dataSources.get(i % dataSources.size());
    }
}
