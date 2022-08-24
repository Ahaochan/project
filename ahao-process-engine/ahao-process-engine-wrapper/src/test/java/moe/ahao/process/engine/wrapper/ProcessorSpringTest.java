package moe.ahao.process.engine.wrapper;

import moe.ahao.process.engine.core.process.ProcessContext;
import moe.ahao.process.engine.core.processor.DynamicProcessorDemo;
import moe.ahao.process.engine.core.processor.RollbackOneProcessorDemo;
import moe.ahao.process.engine.core.processor.RollbackTwoProcessorDemo;
import moe.ahao.process.engine.core.processor.StandardProcessorDemo;
import moe.ahao.process.engine.wrapper.config.EnableProcessEngine;
import moe.ahao.process.engine.wrapper.model.ProcessContextFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = ProcessorSpringTest.TestConfig.class)
@EnableProcessEngine("process-demo.xml")
class ProcessorSpringTest {

    @Autowired
    private ProcessContextFactory processContextFactory;

    @Configuration(proxyBeanMethods = false)
    static class TestConfig {
        @Bean
        @Scope(SCOPE_PROTOTYPE)
        public DynamicProcessorDemo dynamicProcessorDemo() {
            return new DynamicProcessorDemo();
        }

        @Bean
        @Scope(SCOPE_PROTOTYPE)
        public RollbackOneProcessorDemo rollbackOneProcessorDemo() {
            return new RollbackOneProcessorDemo();
        }

        @Bean
        @Scope(SCOPE_PROTOTYPE)
        public RollbackTwoProcessorDemo rollbackTwoProcessorDemo() {
            return new RollbackTwoProcessorDemo();
        }

        @Bean
        @Scope(SCOPE_PROTOTYPE)
        public StandardProcessorDemo standardProcessorDemo() {
            return new StandardProcessorDemo();
        }
    }

    @Test
    @DisplayName("标准流程")
    void standard() throws Exception {
        ProcessContext context = processContextFactory.getContext("standard");
        context.set("id", 123456);
        context.set("path", new ArrayList<>());

        context.start();
        List<String> expect = Arrays.asList("A", "B", "C");
        Assertions.assertIterableEquals(expect, context.get("path"));
    }
}
