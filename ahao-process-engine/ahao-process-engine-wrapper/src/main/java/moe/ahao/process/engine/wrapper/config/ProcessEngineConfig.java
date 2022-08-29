package moe.ahao.process.engine.wrapper.config;

import moe.ahao.process.engine.core.refresh.ProcessRefreshPolicy;
import moe.ahao.process.engine.core.store.ProcessStateDAO;
import moe.ahao.process.engine.core.store.ProcessStateStore;
import moe.ahao.process.engine.wrapper.instance.ProcessorCreator;
import moe.ahao.process.engine.wrapper.instance.SpringBeanInstanceCreator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;

public class ProcessEngineConfig {
    @Bean(ProcessorCreator.BEAN_NAME)
    public ProcessorCreator processorCreator() {
        return new SpringBeanInstanceCreator();
    }

    @Bean
    public ProcessStateStore processStateStore(ObjectProvider<ProcessStateDAO> processStateDAOOP, ObjectProvider<ProcessRefreshPolicy> processRefreshPolicyOP) {
        ProcessStateDAO processStateDAO = processStateDAOOP.getIfAvailable();
        ProcessRefreshPolicy processRefreshPolicy = processRefreshPolicyOP.getIfAvailable();

        if(processStateDAO == null && processRefreshPolicy == null) {
            return new ProcessStateStore();
        } else if(processStateDAO != null) {
            return new ProcessStateStore(processStateDAO);
        } else if (processRefreshPolicy != null) {
            return new ProcessStateStore(processRefreshPolicy);
        }
        return new ProcessStateStore();
    }
}
