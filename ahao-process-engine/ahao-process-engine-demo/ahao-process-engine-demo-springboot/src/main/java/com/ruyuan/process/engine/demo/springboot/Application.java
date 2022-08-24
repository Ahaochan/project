package com.ruyuan.process.engine.demo.springboot;

import com.ruyuan.process.engine.annoations.EnableProcessEngine;
import com.ruyuan.process.engine.config.StringXmlProcessParser;
import com.ruyuan.process.engine.model.ProcessContextFactory;
import com.ruyuan.process.engine.process.ProcessContext;
import com.ruyuan.process.engine.store.ContinueProcessRefreshPolicy;
import com.ruyuan.process.engine.store.DefaultProcessStateStore;
import com.ruyuan.process.engine.store.NoOpProcessStateStore;
import com.ruyuan.process.engine.store.ProcessStateStore;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@RestController
@SpringBootApplication
@EnableProcessEngine("process-demo.xml")
public class Application {

    @Autowired
    private ProcessContextFactory processContextFactory;

    @Bean
    public ProcessStateStore processStateStore() {
        return new DefaultProcessStateStore(redissonClient(), new ContinueProcessRefreshPolicy());
//        return new NoOpProcessStateStore();
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


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/test1")
    public String test1() {
        ProcessContext process1 = processContextFactory.getContext("process1");
        process1.set("nextId", "node4");
        process1.start();

        return "true";
    }

    @GetMapping("/test2")
    public String test2() throws Exception {
        ProcessContext process1 = processContextFactory.getContext("process1");
        log.info("before refresh process......");
        process1.set("id", "process1");
        process1.start();
        String dynamicXmlConfig = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<process-context " +
                "xmlns=\"http://www.w3school.com.cn\"\n" + "                 xmlns:xsi=\"http://www.w3" +
                ".org/2001/XMLSchema-instance\"\n" + "                 xsi:schemaLocation=\"http://www.w3school.com" +
                ".cn process-engine.xsd\">\n" + "    <process name=\"process1\">\n" + "        <node name=\"node1\" " +
                "class=\"com.ruyuan.process.engine.demo.springboot.processor" + ".StandardProcessorDemo\" " +
                "next=\"node2\" begin=\"true\"/>\n" + "        <node name=\"node2\" class=\"com.ruyuan.process.engine" +
                ".demo.springboot.processor" + ".StandardProcessorDemo\"/>\n" + "    </process>\n" + "\n" +
                "</process-context>";
        StringXmlProcessParser stringXmlProcessParser = new StringXmlProcessParser(dynamicXmlConfig);
        processContextFactory.refresh(stringXmlProcessParser.parse());
        process1 = processContextFactory.getContext("process1");
        log.info("after refresh process......");
        process1.set("id", "process1");
        process1.start();
        return "true";
    }
}
