package moe.ahao.spring.cloud.stream.rocketmq.base;

import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.config.BindingServiceConfiguration;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Import({TestConfig.AhaoStreamListener.class,
    // ChannelBindingAutoConfiguration.class,
    BindingServiceConfiguration.class,
    JacksonAutoConfiguration.class
})
public class TestConfig {

    public static class AhaoStreamListener {
        public List<String> result = new ArrayList<>();

        @StreamListener(Processor.INPUT)
        public void receive(String message) {
            System.out.println("接收到:" + message);
            result.add(message);
            BaseTest.latch.countDown();
//            throw new IllegalArgumentException("异常测试");
        }

        // @StreamListener("errorChannel")
        // public void error(ErrorMessage message) {
        //     System.out.println("错误:"+message);
        // }
    }
}
