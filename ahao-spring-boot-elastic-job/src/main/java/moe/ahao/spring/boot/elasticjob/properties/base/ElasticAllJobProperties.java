package moe.ahao.spring.boot.elasticjob.properties.base;

import moe.ahao.spring.boot.elasticjob.properties.DataFlowJobProperties;
import moe.ahao.spring.boot.elasticjob.properties.ScriptJobProperties;
import moe.ahao.spring.boot.elasticjob.properties.SimpleJobProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties("elastic-job")
public class ElasticAllJobProperties {
    @NestedConfigurationProperty
    private DefaultJobProperties base;
    @NestedConfigurationProperty
    private List<SimpleJobProperties> simple = new ArrayList<>();
    @NestedConfigurationProperty
    private List<DataFlowJobProperties> dataFlow = new ArrayList<>();
    @NestedConfigurationProperty
    private List<ScriptJobProperties> script = new ArrayList<>();

    public DefaultJobProperties getBase() {
        return base;
    }

    public void setBase(DefaultJobProperties base) {
        this.base = base;
    }

    public List<SimpleJobProperties> getSimple() {
        return simple;
    }

    public void setSimple(List<SimpleJobProperties> simple) {
        this.simple = simple;
    }

    public List<DataFlowJobProperties> getDataFlow() {
        return dataFlow;
    }

    public void setDataFlow(List<DataFlowJobProperties> dataFlow) {
        this.dataFlow = dataFlow;
    }

    public List<ScriptJobProperties> getScript() {
        return script;
    }

    public void setScript(List<ScriptJobProperties> script) {
        this.script = script;
    }
}
