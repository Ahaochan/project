package moe.ahao.process.engine.core.store;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public interface ProcessStateDAO {
    String BEAN_NAME = "processStateStore";
    /**
     * 记录流程的元数据, 并记录最后一次更新时间
     *
     * @param globalUniqueId 流程全局唯一id
     * @param key            流程的元数据key
     * @param value          流程的元数据value
     */
    default void put(String globalUniqueId, String key, String value) {
        this.put(globalUniqueId, Collections.singletonMap(key, value));
    }

    /**
     * 记录流程的元数据, 并记录最后一次更新时间
     *
     * @param globalUniqueId 流程全局唯一id
     * @param params         流程的元数据
     */
    void put(String globalUniqueId, Map<String, String> params);

    /**
     * 删除流程的元数据
     *
     * @param globalUniqueId 流程全局唯一id
     */
    void remove(String globalUniqueId);

    /**
     * 获取元数据
     *
     * @param globalUniqueId 流程全局唯一id
     * @return 元数据
     */
    Map<String, String> getMap(String globalUniqueId);

    /**
     * 获取超时未完成的流程信息
     * 流程执行过程中，会不断更新时间戳。
     *
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return 未完成流程的globalUniqueId列表
     */
    List<String> pollUnCompletedProcess(int timeout, TimeUnit unit);
}
