package moe.ahao.process.engine.core.store;

import moe.ahao.process.engine.core.enums.ProcessStatusEnum;
import moe.ahao.process.engine.core.refresh.AbortProcessRefreshPolicy;
import moe.ahao.process.engine.core.refresh.ProcessRefreshPolicy;
import moe.ahao.util.commons.io.JSONHelper;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 负责保存任务执行状态的组件
 */
public class ProcessStateStore {
    public static final String META_CURRENT_EXECUTE_STATE_KEY = "current-execute-state";
    public static final String META_CURRENT_EXECUTE_NODE_KEY = "current-execute-node";
    public static final String META_PROCESS_NAME_KEY = "process-name";
    public static final String META_PROCESS_PARAM_KEY = "process-param";

    private final ProcessRefreshPolicy processRefreshPolicy;
    private final ProcessStateDAO processStateDAO;

    public ProcessStateStore() {
        this(new NoOpProcessStateDAO(), new AbortProcessRefreshPolicy());
    }

    public ProcessStateStore(ProcessStateDAO processStateDAO) {
        this(processStateDAO, new AbortProcessRefreshPolicy());
    }

    public ProcessStateStore(ProcessRefreshPolicy processRefreshPolicy) {
        this(new NoOpProcessStateDAO(), processRefreshPolicy);
    }

    public ProcessStateStore(ProcessStateDAO processStateDAO, ProcessRefreshPolicy processRefreshPolicy) {
        this.processStateDAO = processStateDAO;
        this.processRefreshPolicy = processRefreshPolicy;
    }

    /**
     * 初始化流程的元数据信息
     *
     * @param globalUniqueId 流程的全局唯一id
     * @param processorName  流程的名称
     */
    public void initProcessMetadata(String globalUniqueId, String processorName) {
        // 1. 初始化流程节点的执行状态为正向执行, 并记录流程的名称
        Map<String, String> metadata = new HashMap<>();
        metadata.put(META_CURRENT_EXECUTE_STATE_KEY, ProcessStatusEnum.PROCESS.getName());
        metadata.put(META_PROCESS_NAME_KEY, processorName);
        processStateDAO.put(globalUniqueId, metadata);
    }

    /**
     * 更新流程的执行状态, 执行到哪个节点了
     *
     * @param globalUniqueId 流程的全局唯一id
     * @param nodeName       执行的节点名称
     * @param params         流程的全局参数
     */
    public void updateExecutionNodeMetadata(String globalUniqueId, String nodeName, Map<String, Object> params) {
        // 1. 存储当前节点的信息
        processStateDAO.put(globalUniqueId, META_CURRENT_EXECUTE_NODE_KEY, nodeName);

        // 2. 记录流程上下文的参数的信息
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String paramKey = entry.getKey();
            String paramClassName = entry.getValue().getClass().getCanonicalName();
            String paramJSONValue = JSONHelper.toString(entry.getValue());
            String paramsKey = META_PROCESS_PARAM_KEY + "#" + paramKey + "#" + paramClassName;
            processStateDAO.put(globalUniqueId, paramsKey, paramJSONValue);
        }
    }

    /**
     * 流程发生了异常, 标记流程为回滚状态
     *
     * @param globalUniqueId 流程的全局唯一id
     * @param params         流程的全局参数
     */
    public void updateRollbackMetadata(String globalUniqueId, Map<String, Object> params) {
        // 1. 存储当前节点的信息
        processStateDAO.put(globalUniqueId, META_CURRENT_EXECUTE_STATE_KEY, ProcessStatusEnum.ROLLBACK.getName());

        // 2. 记录流程上下文的参数的信息
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String paramKey = entry.getKey();
            String paramClassName = entry.getValue().getClass().getCanonicalName();
            String paramJSONValue = JSONHelper.toString(entry.getValue());
            String paramsKey = META_PROCESS_PARAM_KEY + "#" + paramKey + "#" + paramClassName;
            processStateDAO.put(globalUniqueId, paramsKey, paramJSONValue);
        }
    }

    /**
     * 删除该流程的所有元数据信息
     * @param globalUniqueId 流程的全局唯一id
     */
    public void clearMetadata(String globalUniqueId) {
        processStateDAO.remove(globalUniqueId);
    }

    public Collection<String> pollUnCompletedProcess(int timeout, TimeUnit unit, boolean refresh) {
        List<String> globalUniqueIds = processStateDAO.pollUnCompletedProcess(timeout, unit);
        List<String> result = new ArrayList<>();
        for (String globalUniqueId : globalUniqueIds) {
            // 过滤配置刷新后, 不允许继续的流程
            if (refresh && !processRefreshPolicy.continueExecuteProcess(globalUniqueId, this.getMap(globalUniqueId))) {
                this.clearMetadata(globalUniqueId);
                continue;
            }
            result.add(globalUniqueId);
        }
        return result;
    }

    public Map<String, String> getMap(String key) {
        return processStateDAO.getMap(key);
    }
}
