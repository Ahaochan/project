package moe.ahao.process.engine.wrapper.store;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.engine.core.refresh.AbortProcessRefreshPolicy;
import moe.ahao.process.engine.core.refresh.ProcessRefreshPolicy;
import moe.ahao.process.engine.core.store.ProcessStateDAO;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 默认的流程管理器
 */
@Slf4j
public class RedisProcessStateStore implements ProcessStateDAO {
    private static final String REDIS_KEY_PROCESSES = "ahao-process-engine:processes";
    private final RedissonClient redissonClient;
    private final ProcessRefreshPolicy processRefreshPolicy;

    public RedisProcessStateStore(RedissonClient redissonClient) {
        this(redissonClient, new AbortProcessRefreshPolicy());
    }
    public RedisProcessStateStore(RedissonClient redissonClient, ProcessRefreshPolicy processRefreshPolicy) {
        this.redissonClient = redissonClient;
        this.processRefreshPolicy = processRefreshPolicy;
    }

    @Override
    public void put(String globalUniqueId, Map<String, String> params) {
        log.debug("recordProcessMetadata name={}, metadata={}", globalUniqueId, params);
        // 1. 将当前节点的信息以key、value的形式存入redis里的hash数据结构
        RMap<Object, Object> map = redissonClient.getMap(globalUniqueId);
        map.putAll(params);

        // 2. 重置当前节点的更新时间戳, 放回到zset里面
        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet(REDIS_KEY_PROCESSES);
        scoredSortedSet.remove(globalUniqueId);
        LocalDateTime time = LocalDateTime.now();
        long timeOfSecond = time.toEpochSecond(ZoneOffset.ofHours(8));
        scoredSortedSet.add(timeOfSecond, globalUniqueId);
    }

    @Override
    public void remove(String globalUniqueId) {
        log.debug("clearMetadata name={}", globalUniqueId);
        RMap<Object, Object> map = redissonClient.getMap(globalUniqueId);
        map.delete();

        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet(REDIS_KEY_PROCESSES);
        scoredSortedSet.remove(globalUniqueId);
    }

    @Override
    public Map<String, String> getMap(String globalUniqueId) {
        return redissonClient.getMap(globalUniqueId);
    }

    @Override
    public List<String> pollUnCompletedProcess(int timeout, TimeUnit unit) {
        // 1. 从zset里取出所有正在执行的流程
        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet(REDIS_KEY_PROCESSES);
        // 2. 计算出timeout时间之前的时间戳
        LocalDateTime time = LocalDateTime.now();
        long timeOfSecond = time.toEpochSecond(ZoneOffset.ofHours(8));
        long offset = unit.toSeconds(timeout);
        double endScore = timeOfSecond - offset;
        // 3. 从zset按照分数范围查询, 取出timeout时间范围内的流程
        Collection<String> keys = scoredSortedSet.valueRange(0, true, endScore, true);
        return new ArrayList<>(keys);
    }
}
