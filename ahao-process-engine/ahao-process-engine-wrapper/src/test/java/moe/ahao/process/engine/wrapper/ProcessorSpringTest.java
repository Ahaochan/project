package moe.ahao.process.engine.wrapper;

import moe.ahao.process.engine.core.ProcessContext;
import moe.ahao.process.engine.core.processor.DynamicProcessorNodeDemo;
import moe.ahao.process.engine.core.processor.RollbackOneProcessorNodeDemo;
import moe.ahao.process.engine.core.processor.RollbackTwoProcessorNodeDemo;
import moe.ahao.process.engine.core.processor.StandardProcessorNodeDemo;
import moe.ahao.process.engine.wrapper.config.EnableProcessEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = ProcessorSpringTest.TestConfig.class)
@EnableProcessEngine("process-demo.xml")
class ProcessorSpringTest {

    @Autowired
    private ProcessContextFactory processContextFactory;

    @Configuration(proxyBeanMethods = false)
    static class TestConfig {
        @Bean
        @Scope(SCOPE_PROTOTYPE)
        public DynamicProcessorNodeDemo dynamicProcessorDemo() {
            DynamicProcessorNodeDemo node = new DynamicProcessorNodeDemo();
            node.setName("动态节点");
            return node;
        }

        @Bean
        @Scope(SCOPE_PROTOTYPE)
        public RollbackOneProcessorNodeDemo rollbackOneProcessorDemo() {
            RollbackOneProcessorNodeDemo node = new RollbackOneProcessorNodeDemo();
            node.setName("回滚节点1");
            return node;
        }

        @Bean
        @Scope(SCOPE_PROTOTYPE)
        public RollbackTwoProcessorNodeDemo rollbackTwoProcessorDemo() {
            RollbackTwoProcessorNodeDemo node = new RollbackTwoProcessorNodeDemo();
            node.setName("回滚节点2");
            return node;
        }

        @Bean
        @Scope(SCOPE_PROTOTYPE)
        public StandardProcessorNodeDemo standardProcessorDemo() {
            StandardProcessorNodeDemo node = new StandardProcessorNodeDemo();
            node.setName("标准节点");
            return node;
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
