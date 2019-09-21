package moe.ahao.spring.boot.elasticjob.properties.base;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.executor.handler.JobProperties;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class BaseJobProperties {
    // ============================================ JobCoreConfiguration ============================================
    private String jobName;
    private String cron;
    private Integer shardingTotalCount;
    private String shardingItemParameters;
    private String jobParameter;
    private Boolean failover;
    private Boolean misfire;
    private String description;
    private String jobExceptionHandler;
    private String executorServiceHandler;

    public JobCoreConfiguration generateJobCoreConfig(DefaultJobProperties defaultConfig) {
        // 1. 使用 默认值 覆盖未填写的属性
        String jobName = this.jobName;
        String cron = this.cron;
        int shardingTotalCount = ObjectUtils.defaultIfNull(this.shardingTotalCount, defaultConfig.getShardingTotalCount());
        String shardingItemParameters = StringUtils.defaultIfBlank(this.shardingItemParameters, defaultConfig.getShardingItemParameters());
        String jobParameter = StringUtils.defaultIfBlank(this.jobParameter, defaultConfig.getJobParameter());
        boolean failover = ObjectUtils.defaultIfNull(this.failover, defaultConfig.isFailover());
        boolean misfire = ObjectUtils.defaultIfNull(this.misfire, defaultConfig.isMisfire());
        String description = StringUtils.defaultIfBlank(this.description, defaultConfig.getDescription());
        String jobExceptionHandler = StringUtils.defaultIfBlank(this.jobExceptionHandler, defaultConfig.getJobExceptionHandler());
        String executorServiceHandler = StringUtils.defaultIfBlank(this.executorServiceHandler, defaultConfig.getExecutorServiceHandler());

        // 2. 创建 JobCoreConfiguration
        JobCoreConfiguration.Builder builder = JobCoreConfiguration.newBuilder(jobName, cron, shardingTotalCount)
            .shardingItemParameters(shardingItemParameters)
            .jobParameter(jobParameter)
            .failover(failover)
            .misfire(misfire)
            .description(description)
            .jobProperties(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(), jobExceptionHandler)
            .jobProperties(JobProperties.JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER.getKey(), executorServiceHandler);
        return builder.build();
    }
    // ============================================ JobCoreConfiguration ============================================

    // ============================================ JobCoreConfiguration ============================================
    public JobTypeConfiguration generateJobTypeConfig(JobCoreConfiguration coreConfig, Class jobClass) {
        throw new UnsupportedOperationException("必须交由子类实现");
    }
    // ============================================ JobCoreConfiguration ============================================


    // ============================================ LiteJobConfiguration ============================================
    private Boolean monitorExecution;
    private Integer maxTimeDiffSeconds;
    private Integer monitorPort;
    private String jobShardingStrategyClass;
    private Boolean disabled;
    private Boolean overwrite;
    private Integer reconcileIntervalMinutes;

    public LiteJobConfiguration generateLiteJobConfig(DefaultJobProperties defaultConfig, JobTypeConfiguration typeConfig) {
        // 1. 使用 默认值 覆盖未填写的属性
        boolean monitorExecution = ObjectUtils.defaultIfNull(this.monitorExecution, defaultConfig.isMonitorExecution());
        int monitorPort = ObjectUtils.defaultIfNull(this.monitorPort, defaultConfig.getMonitorPort());
        int maxTimeDiffSeconds = ObjectUtils.defaultIfNull(this.maxTimeDiffSeconds, defaultConfig.getMaxTimeDiffSeconds());
        String jobShardingStrategyClass = StringUtils.defaultIfBlank(this.jobShardingStrategyClass, defaultConfig.getJobShardingStrategyClass());
        int reconcileIntervalMinutes = ObjectUtils.defaultIfNull(this.reconcileIntervalMinutes, defaultConfig.getReconcileIntervalMinutes());
        boolean overwrite = ObjectUtils.defaultIfNull(this.overwrite, defaultConfig.isOverwrite());
        boolean disabled = ObjectUtils.defaultIfNull(this.disabled, defaultConfig.isDisabled());

        // 2. 创建 LiteJobConfiguration
        LiteJobConfiguration.Builder builder = LiteJobConfiguration.newBuilder(typeConfig)
            .monitorExecution(monitorExecution)
            .monitorPort(monitorPort)
            .maxTimeDiffSeconds(maxTimeDiffSeconds)
            .jobShardingStrategyClass(jobShardingStrategyClass)
            .reconcileIntervalMinutes(reconcileIntervalMinutes)
            .overwrite(overwrite)
            .disabled(disabled);
        return builder.build();
    }
    // ============================================ LiteJobConfiguration ============================================

    // ============================================ SpringJobScheduler ============================================
    private String beanName;
    private Boolean jobEventTraceEnabled;
    // ============================================ SpringJobScheduler ============================================


    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Integer getShardingTotalCount() {
        return shardingTotalCount;
    }

    public void setShardingTotalCount(Integer shardingTotalCount) {
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

    public Boolean getFailover() {
        return failover;
    }

    public void setFailover(Boolean failover) {
        this.failover = failover;
    }

    public Boolean getMisfire() {
        return misfire;
    }

    public void setMisfire(Boolean misfire) {
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

    public Boolean getMonitorExecution() {
        return monitorExecution;
    }

    public void setMonitorExecution(Boolean monitorExecution) {
        this.monitorExecution = monitorExecution;
    }

    public Integer getMaxTimeDiffSeconds() {
        return maxTimeDiffSeconds;
    }

    public void setMaxTimeDiffSeconds(Integer maxTimeDiffSeconds) {
        this.maxTimeDiffSeconds = maxTimeDiffSeconds;
    }

    public Integer getMonitorPort() {
        return monitorPort;
    }

    public void setMonitorPort(Integer monitorPort) {
        this.monitorPort = monitorPort;
    }

    public String getJobShardingStrategyClass() {
        return jobShardingStrategyClass;
    }

    public void setJobShardingStrategyClass(String jobShardingStrategyClass) {
        this.jobShardingStrategyClass = jobShardingStrategyClass;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
    }

    public Integer getReconcileIntervalMinutes() {
        return reconcileIntervalMinutes;
    }

    public void setReconcileIntervalMinutes(Integer reconcileIntervalMinutes) {
        this.reconcileIntervalMinutes = reconcileIntervalMinutes;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Boolean getJobEventTraceEnabled() {
        return jobEventTraceEnabled;
    }

    public void setJobEventTraceEnabled(Boolean jobEventTraceEnabled) {
        this.jobEventTraceEnabled = jobEventTraceEnabled;
    }
}
