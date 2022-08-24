package moe.ahao.process.engine.wrapper.config;

import moe.ahao.process.engine.wrapper.instance.ProcessorCreator;
import moe.ahao.process.engine.wrapper.instance.SpringBeanInstanceCreator;
import org.springframework.context.annotation.Bean;

public class ProcessEngineConfig {
    @Bean
    public ProcessorCreator processorCreator() {
        return new SpringBeanInstanceCreator();
    }
}
