package moe.ahao.spring.boot.elasticjob.properties;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import moe.ahao.spring.boot.elasticjob.properties.base.BaseJobProperties;
import org.apache.http.annotation.Obsolete;

public class ScriptJobProperties extends BaseJobProperties {
    private String scriptCommandLine;

    @Obsolete
    public JobTypeConfiguration generateJobTypeConfig(JobCoreConfiguration coreConfig, Class jobClass) {
        JobTypeConfiguration typeConfig = new ScriptJobConfiguration(coreConfig, scriptCommandLine);
        return typeConfig;
    }

    public String getScriptCommandLine() {
        return scriptCommandLine;
    }

    public void setScriptCommandLine(String scriptCommandLine) {
        this.scriptCommandLine = scriptCommandLine;
    }
}
