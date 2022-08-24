package com.ruyuan.process.engine.store;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 默认的流程管理器
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
public class DefaultProcessStateStore implements ProcessStateStore {

    private final RedissonClient redissonClient;

    private static final String REDIS_KEY_PROCESSES = "ruyuan-process-engine:processes";
    private final ProcessRefreshPolicy processRefreshPolicy;

    public DefaultProcessStateStore(RedissonClient redissonClient) {
        this(redissonClient, new AbortProcessRefreshPolicy());
    }


    public DefaultProcessStateStore(RedissonClient redissonClient, ProcessRefreshPolicy processRefreshPolicy) {
        this.redissonClient = redissonClient;
        this.processRefreshPolicy = processRefreshPolicy;
    }

    @Override
    public void recordProcessMetadata(String name, Map<String, String> metadata) {
        log.debug("recordProcessMetadata name={}, metadata={}", name, metadata);
        RMap<Object, Object> map = redissonClient.getMap(name);
        map.putAll(metadata);

        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet(REDIS_KEY_PROCESSES);
        LocalDateTime time = LocalDateTime.now();
        long timeOfSecond = time.toEpochSecond(ZoneOffset.ofHours(8));
        scoredSortedSet.add(timeOfSecond, name);
    }

    @Override
    public void updateMetadata(String name, String key, String value) {
        log.debug("updateMetadata name={}, key={}, value={}", name, key, value);
        RMap<Object, Object> map = redissonClient.getMap(name);
        map.put(key, value);

        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet(REDIS_KEY_PROCESSES);
        scoredSortedSet.remove(name);
        LocalDateTime time = LocalDateTime.now();
        long timeOfSecond = time.toEpochSecond(ZoneOffset.ofHours(8));
        scoredSortedSet.add(timeOfSecond, name);
    }

    @Override
    public void clearMetadata(String name) {
        log.debug("clearMetadata name={}", name);
        RMap<Object, Object> map = redissonClient.getMap(name);
        map.delete();

        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet(REDIS_KEY_PROCESSES);
        scoredSortedSet.remove(name);
    }

    @Override
    public boolean lock(String key) {
        String lockKey = "locked:" + key;
        RLock fairLock = redissonClient.getFairLock(lockKey);
        return fairLock.tryLock();
    }

    @Override
    public void unlock(String key) {
        String lockKey = "locked:" + key;
        RLock fairLock = redissonClient.getFairLock(lockKey);
        if (fairLock.isLocked()) {
            fairLock.unlock();
        }
    }

    @Override
    public Collection<String> pollUnCompletedProcess(int timeout, TimeUnit unit, boolean refresh) {
        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet(REDIS_KEY_PROCESSES);
        LocalDateTime time = LocalDateTime.now();
        long timeOfSecond = time.toEpochSecond(ZoneOffset.ofHours(8));
        long offset = unit.toSeconds(timeout);
        double endScore = timeOfSecond - offset;
        Collection<String> keys = scoredSortedSet.valueRange(0, true, endScore, true);
        if (!refresh) {
            return keys;
        } else {
            Set<String> result = new HashSet<>();
            for (String key : keys) {
                if (processRefreshPolicy.continueExecuteProcess(key, getMap(key))) {
                    result.add(key);
                }else{
                    clearMetadata(key);
                }
            }
            return result;
        }
    }

    @Override
    public Map<String, String> getMap(String key) {
        return redissonClient.getMap(key);
    }
}
