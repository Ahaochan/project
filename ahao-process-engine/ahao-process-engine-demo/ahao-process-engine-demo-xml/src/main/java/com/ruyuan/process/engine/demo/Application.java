package com.ruyuan.process.engine.demo;

import com.ruyuan.process.engine.ProcessEngine;
import com.ruyuan.process.engine.config.ClassPathXmlProcessParser;
import com.ruyuan.process.engine.config.StringXmlProcessParser;
import com.ruyuan.process.engine.process.ProcessContext;
import com.ruyuan.process.engine.store.DefaultProcessStateStore;
import com.ruyuan.process.engine.store.ProcessStateStore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public class Application {
    public static void main(String[] args) throws Exception {
        usingClasspathXmlProcessParser();
        // usingStringXmlProcessParser();
    }

    private static void usingClasspathXmlProcessParser() throws Exception {
        ProcessStateStore store = new DefaultProcessStateStore(redissonClient());
        ProcessEngine processEngine = new ProcessEngine(new ClassPathXmlProcessParser("process-demo.xml"), store);
        ProcessContext process1 = processEngine.getContext("process2");
        TestParam testParam = new TestParam("1", "23");
        process1.set("param", testParam);
        process1.set("id", "process2");
        process1.start();
    }

    private static void usingStringXmlProcessParser() throws Exception {
        String xmlConfig = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<process-context xmlns=\"http://www.w3school.com.cn\"\n" +
                "                 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "                 xsi:schemaLocation=\"http://www.w3school.com.cn process-engine.xsd\">\n" +
                "    <process name=\"process1\">\n" +
                "        <node name=\"node1\" class=\"com.ruyuan.process.engine.demo.StandardProcessorDemo\" next=\"node2\" begin=\"true\"/>\n" +
                "        <node name=\"node2\" class=\"com.ruyuan.process.engine.demo.RollBackProcessorDemo\" next=\"node3\"/>\n" +
                "        <node name=\"node3\" class=\"com.ruyuan.process.engine.demo.DynamicProcessorDemo\" next=\"node4,node5\"/>\n" +
                "        <node name=\"node4\" class=\"com.ruyuan.process.engine.demo.RollbackProcessorThrowExceptionDemo\"/>\n" +
                "        <node name=\"node5\" class=\"com.ruyuan.process.engine.demo.StandardProcessorDemo\"/>\n" +
                "    </process>\n" +
                "\n" +
                "    <process name=\"process-async\">\n" +
                "        <node name=\"node1\" class=\"com.ruyuan.process.engine.demo.StandardProcessorDemo\" next=\"node2\" begin=\"true\"/>\n" +
                "        <node name=\"node2\" class=\"com.ruyuan.process.engine.demo.RollBackProcessorDemo\" next=\"node3\"/>\n" +
                "        <node name=\"node3\" class=\"com.ruyuan.process.engine.demo.DynamicProcessorDemo\" next=\"node4,node5\" invoke-method=\"async\"/>\n" +
                "        <node name=\"node4\" class=\"com.ruyuan.process.engine.demo.RollbackProcessorThrowExceptionDemo\"/>\n" +
                "        <node name=\"node5\" class=\"com.ruyuan.process.engine.demo.StandardProcessorDemo\"/>\n" +
                "    </process>\n" +
                "\n" +
                "</process-context>";
        ProcessEngine processEngine = new ProcessEngine(new StringXmlProcessParser(xmlConfig));
        ProcessContext process1 = processEngine.getContext("process-async");
        process1.set("nextId", "node4");
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
