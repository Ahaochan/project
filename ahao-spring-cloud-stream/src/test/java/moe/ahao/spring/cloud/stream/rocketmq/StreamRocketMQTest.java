package moe.ahao.spring.cloud.stream.rocketmq;

import com.alibaba.cloud.stream.binder.rocketmq.config.RocketMQBinderAutoConfiguration;
import moe.ahao.spring.cloud.stream.rocketmq.base.BaseTest;
import moe.ahao.spring.cloud.stream.rocketmq.base.TestConfig;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {TestConfig.class,
    RocketMQAutoConfiguration.class, RocketMQBinderAutoConfiguration.class,})

@ActiveProfiles("rocketmq")
public class StreamRocketMQTest extends BaseTest {
    @Test
    public void test() throws Exception {
        super.test(10);
    }
}
