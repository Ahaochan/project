package moe.ahao.process.engine.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import moe.ahao.process.engine.core.process.ProcessContext;
import moe.ahao.process.engine.core.store.DefaultProcessStateStore;
import moe.ahao.process.engine.core.store.ProcessStateStore;
import moe.ahao.process.engine.wrapper.parse.ClassPathXmlProcessParser;
import moe.ahao.process.engine.wrapper.parse.ProcessParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcessorDefinitionXmlTest {
    @Test
    @DisplayName("没有起始节点")
    void noFirst() {
        ProcessParser parser = new ClassPathXmlProcessParser("process-no-begin.xml");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ProcessEngine(parser));
    }

    @Test
    @DisplayName("流程成环判断")
    public void ring() throws Exception {
        ProcessParser parser = new ClassPathXmlProcessParser("process-ring.xml");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ProcessEngine(parser));
    }

    @Test
    @DisplayName("标准流程")
    public void standard() throws Exception {
        ProcessParser parser = new ClassPathXmlProcessParser("process-demo.xml");
        ProcessEngine processEngine = new ProcessEngine(parser);

        ProcessContext context = processEngine.getContext("standard");
        context.set("id", 123456);
        context.set("path", new ArrayList<>());

        context.start();
        List<String> expect = Arrays.asList("A", "B", "C");
        Assertions.assertIterableEquals(expect, context.get("path"));
    }

    @Test
    @DisplayName("回滚流程")
    public void rollback() throws Exception {
        ProcessParser parser = new ClassPathXmlProcessParser("process-demo.xml");
        ProcessEngine processEngine = new ProcessEngine(parser);

        ProcessContext context = processEngine.getContext("rollback");
        context.set("id", 123456);
        context.set("path", new ArrayList<>());

        Assertions.assertThrows(RuntimeException.class, context::start);
        List<String> expect = Arrays.asList("A", "B", "C", "C", "B");
        Assertions.assertIterableEquals(expect, context.get("path"));
    }

    @Test
    @DisplayName("动态流程")
    public void dynamic() throws Exception {
        ProcessParser parser = new ClassPathXmlProcessParser("process-demo.xml");
        ProcessEngine processEngine = new ProcessEngine(parser);

        ProcessContext context = processEngine.getContext("dynamic");
        context.set("id", 123456);
        context.set("path", new ArrayList<>());

        context.start();
        List<String> expect = Arrays.asList("A", "B", "C", "D");
        Assertions.assertIterableEquals(expect, context.get("path"));
    }

    @Test
    public void store() throws Exception {
        ProcessStateStore store = new DefaultProcessStateStore(redissonClient());
        ProcessEngine processEngine = new ProcessEngine(new ClassPathXmlProcessParser("process-demo.xml"), store);

        ProcessContext process1 = processEngine.getContext("process2");

        TestParam testParam = new TestParam("1", "23");
        process1.set("param", testParam);
        process1.set("id", "process2");
        process1.start();
    }

    private static RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379")
            .setConnectionMinimumIdleSize(10)
            .setConnectionPoolSize(100)
            .setIdleConnectionTimeout(600000)
            .setSubscriptionConnectionMinimumIdleSize(10)
            .setSubscriptionConnectionPoolSize(100)
            .setTimeout(30000);
        config.setCodec(new StringCodec());
        config.setThreads(5);
        config.setNettyThreads(5);
        return Redisson.create(config);
    }

    @Data
    @AllArgsConstructor
    public static class TestParam {
        private String a;
        private String b;
    }
}
