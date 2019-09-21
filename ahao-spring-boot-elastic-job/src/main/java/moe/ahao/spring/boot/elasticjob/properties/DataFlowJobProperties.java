package moe.ahao.spring.boot.elasticjob.properties;

import com.ahao.util.commons.lang.reflect.ClassHelper;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import moe.ahao.spring.boot.elasticjob.properties.base.BaseJobProperties;

public class DataFlowJobProperties extends BaseJobProperties {
    private boolean streamingProcess = false;

    @Override
    public JobTypeConfiguration generateJobTypeConfig(JobCoreConfiguration coreConfig, Class jobClass) {
        Class unwrapClass = ClassHelper.unwrapCglib(jobClass);
        String className = unwrapClass.getName();
        JobTypeConfiguration typeConfig = new DataflowJobConfiguration(coreConfig, className, streamingProcess);
        return typeConfig;
    }

    public Boolean getStreamingProcess() {
        return streamingProcess;
    }

    public void setStreamingProcess(Boolean streamingProcess) {
        this.streamingProcess = streamingProcess;
    }
}
