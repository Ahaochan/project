package moe.ahao.process.engine.core.store;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 负责保存任务执行状态的组件
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public interface ProcessStateStore {

    /**
     * 记录流程的元数据
     *
     * @param name     流程名称
     * @param metadata 元数据
     */
    void recordProcessMetadata(String name, Map<String, String> metadata);

    /**
     * 更新流程的元数据
     *
     * @param name  流程名称
     * @param key   元数据key
     * @param value 元数据value
     */
    void updateMetadata(String name, String key, String value);

    /**
     * 清除元数据
     *
     * @param name 流程名称
     */
    void clearMetadata(String name);

    /**
     * 对同一个流程加锁
     *
     * @param key 锁名称
     * @return 加锁结果
     */
    boolean lock(String key);

    /**
     * 释放锁
     *
     * @param key 锁key
     */
    void unlock(String key);

    /**
     * 获取超时未完成的流程信息
     * 流程执行过程中，会不断更新时间戳。
     * 如果超过一定时间都没更新，则认为改流程是因为机器宕机，导致卡在中间状态了
     *
     * @param timeout 超时时间
     * @param unit    时间单位
     * @param refresh 是否刷新了流程
     * @return 未完成任务的key列表
     */
    Collection<String> pollUnCompletedProcess(int timeout, TimeUnit unit, boolean refresh);

    /**
     * 获取元数据
     *
     * @param key key
     * @return 元数据
     */
    Map<String, String> getMap(String key);
}
