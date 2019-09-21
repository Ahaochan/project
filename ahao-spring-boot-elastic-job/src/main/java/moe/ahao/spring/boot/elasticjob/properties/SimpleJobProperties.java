package moe.ahao.spring.boot.elasticjob.properties;

import com.ahao.util.commons.lang.reflect.ClassHelper;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import moe.ahao.spring.boot.elasticjob.properties.base.BaseJobProperties;

public class SimpleJobProperties extends BaseJobProperties {
    @Override
    public JobTypeConfiguration generateJobTypeConfig(JobCoreConfiguration coreConfig, Class jobClass) {
        Class unwrapClass = ClassHelper.unwrapCglib(jobClass);
        String className = unwrapClass.getName();
        JobTypeConfiguration typeConfig = new SimpleJobConfiguration(coreConfig, className);
        return typeConfig;
    }
}
