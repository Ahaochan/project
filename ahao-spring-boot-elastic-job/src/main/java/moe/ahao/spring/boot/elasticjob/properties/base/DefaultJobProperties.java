package moe.ahao.spring.boot.elasticjob.properties.base;

import com.dangdang.ddframe.job.executor.handler.impl.DefaultExecutorServiceHandler;
import com.dangdang.ddframe.job.executor.handler.impl.DefaultJobExceptionHandler;
import com.dangdang.ddframe.job.lite.api.strategy.impl.AverageAllocationJobShardingStrategy;

public class DefaultJobProperties {
    // ============================================ JobCoreConfiguration ============================================
    /**
     * 默认作业分片总数
     */
    private int shardingTotalCount = 1;
    /**
     * 分片序列号和参数用等号分隔，多个键值对用逗号分隔
     * 分片序列号从0开始，不可大于或等于作业分片总数
     * 如：
     * 0=a,1=b,2=c.
     */
    private String shardingItemParameters = "";
    /**
     * 作业自定义参数
     * 作业自定义参数，可通过传递该参数为作业调度的业务方法传参，用于实现带参数的作业
     * 例：每次获取的数据量、作业实例从数据库读取的主键等.
     */
    private String jobParameter = "";
    /**
     * 是否开启任务执行失效转移，开启表示如果作业在一次任务执行中途宕机，允许将该次未完成的任务在另一作业节点上补偿执行.
     */
    private boolean failover;
    /**
     * 是否开启错过任务重新执行
     */
    private boolean misfire = true;
    /**
     * 作业描述信息
     */
    private String description = "";
    /**
     * 配置jobProperties定义的枚举控制Elastic-Job的实现细节
     * JOB_EXCEPTION_HANDLER用于扩展异常处理类.
     */
    private String jobExceptionHandler = DefaultJobExceptionHandler.class.getName();
    /**
     * 配置jobProperties定义的枚举控制Elastic-Job的实现细节
     * EXECUTOR_SERVICE_HANDLER用于扩展作业处理线程池类.
     */
    private String executorServiceHandler = DefaultExecutorServiceHandler.class.getName();
    // ============================================ JobCoreConfiguration ============================================

    // ============================================ LiteJobConfiguration ============================================
    /**
     * 监控作业运行时状态
     * 每次作业执行时间和间隔时间均非常短的情况，建议不监控作业运行时状态以提升效率。因为是瞬时状态，所以无必要监控。
     * 请用户自行增加数据堆积监控。并且不能保证数据重复选取，应在作业中实现幂等性。
     * 每次作业执行时间和间隔时间均较长的情况，建议监控作业运行时状态，可保证数据不会重复选取.
     */
    private boolean monitorExecution = true;
    /**
     * 作业监控端口
     * 建议配置作业监控端口, 方便开发者dump作业信息。
     * 使用方法: echo “dump” | nc 127.0.0.1 9888.
     */
    private int monitorPort = -1;
    /**
     * 最大允许的本机与注册中心的时间误差秒数
     * 如果时间误差超过配置秒数则作业启动时将抛异常
     * 配置为-1表示不校验时间误差.
     */
    private int maxTimeDiffSeconds = -1;
    /**
     * 作业分片策略实现类全路径
     * 默认使用平均分配策略
     * 详情参见：作业分片策略
     */
    private String jobShardingStrategyClass = AverageAllocationJobShardingStrategy.class.getName();
    /**
     * 修复作业服务器不一致状态服务调度间隔时间，配置为小于1的任意值表示不执行修复
     * 单位：分钟.
     */
    private int reconcileIntervalMinutes = 10;
    /**
     * 设置作业是否启动时禁止.
     * 可用于部署作业时, 先在启动时禁止, 部署结束后统一启动.
     */
    private boolean disabled;
    /**
     * 设置本地配置是否可覆盖注册中心配置.
     * 如果可覆盖, 每次启动作业都以本地配置为准.
     */
    private boolean overwrite;
    // ============================================ LiteJobConfiguration ============================================

    // ============================================ SpringJobScheduler ============================================
    private boolean jobEventTraceEnabled;
    // ============================================ SpringJobScheduler ============================================


    public int getShardingTotalCount() {
        return shardingTotalCount;
    }

    public void setShardingTotalCount(int shardingTotalCount) {
        this.shardingTotalCount = shardingTotalCount;
    }

    public String getShardingItemParameters() {
        return shardingItemParameters;
    }

    public void setShardingItemParameters(String shardingItemParameters) {
        this.shardingItemParameters = shardingItemParameters;
    }

    public String getJobParameter() {
        return jobParameter;
    }

    public void setJobParameter(String jobParameter) {
        this.jobParameter = jobParameter;
    }

    public boolean isFailover() {
        return failover;
    }

    public void setFailover(boolean failover) {
        this.failover = failover;
    }

    public boolean isMisfire() {
        return misfire;
    }

    public void setMisfire(boolean misfire) {
        this.misfire = misfire;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobExceptionHandler() {
        return jobExceptionHandler;
    }

    public void setJobExceptionHandler(String jobExceptionHandler) {
        this.jobExceptionHandler = jobExceptionHandler;
    }

    public String getExecutorServiceHandler() {
        return executorServiceHandler;
    }

    public void setExecutorServiceHandler(String executorServiceHandler) {
        this.executorServiceHandler = executorServiceHandler;
    }

    public boolean isMonitorExecution() {
        return monitorExecution;
    }

    public void setMonitorExecution(boolean monitorExecution) {
        this.monitorExecution = monitorExecution;
    }

    public int getMonitorPort() {
        return monitorPort;
    }

    public void setMonitorPort(int monitorPort) {
        this.monitorPort = monitorPort;
    }

    public int getMaxTimeDiffSeconds() {
        return maxTimeDiffSeconds;
    }

    public void setMaxTimeDiffSeconds(int maxTimeDiffSeconds) {
        this.maxTimeDiffSeconds = maxTimeDiffSeconds;
    }

    public String getJobShardingStrategyClass() {
        return jobShardingStrategyClass;
    }

    public void setJobShardingStrategyClass(String jobShardingStrategyClass) {
        this.jobShardingStrategyClass = jobShardingStrategyClass;
    }

    public int getReconcileIntervalMinutes() {
        return reconcileIntervalMinutes;
    }

    public void setReconcileIntervalMinutes(int reconcileIntervalMinutes) {
        this.reconcileIntervalMinutes = reconcileIntervalMinutes;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public boolean isJobEventTraceEnabled() {
        return jobEventTraceEnabled;
    }

    public void setJobEventTraceEnabled(boolean jobEventTraceEnabled) {
        this.jobEventTraceEnabled = jobEventTraceEnabled;
    }
}
