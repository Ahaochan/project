package moe.ahao.spring.boot.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphO;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SentinelNativeTest {
    public static final int DEFAULT_QPS = 20;
    public static final String RESOURCE = "ahao_resource";

    @BeforeEach
    public void beforeEach() throws Exception {
        // 限流QPS为20
        FlowRule rule = new FlowRule();
        rule.setResource(RESOURCE);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(DEFAULT_QPS);
        FlowRuleManager.loadRules(Arrays.asList(rule));
        Thread.sleep(1000);
    }

    @Test
    public void tryWithResource() throws Exception {
        CountDownLatch latch = new CountDownLatch(DEFAULT_QPS);
        boolean flag = true;
        while (flag) {
            try (Entry entry = SphU.entry(RESOURCE)) {
                System.out.println("开始执行业务逻辑start:" + latch.getCount());
                latch.countDown();
                System.out.println("结束执行业务逻辑end:" + latch.getCount());
            } catch (BlockException e) {
                System.out.println("开始流控逻辑start:" + latch.getCount());
                flag = false;
                System.out.println("结束流控逻辑end:" + latch.getCount());
            }
        }
        Assertions.assertTrue(latch.await(1, TimeUnit.SECONDS));
    }

    @Test
    public void ifElse() throws Exception {
        CountDownLatch latch = new CountDownLatch(DEFAULT_QPS);
        boolean flag = true;
        while (flag) {
            if (SphO.entry(RESOURCE)) {
                try {
                    System.out.println("开始执行业务逻辑start:" + latch.getCount());
                    latch.countDown();
                    System.out.println("结束执行业务逻辑end:" + latch.getCount());
                } finally {
                    SphO.exit();
                }
            } else {
                System.out.println("开始流控逻辑start:" + latch.getCount());
                flag = false;
                System.out.println("结束流控逻辑end:" + latch.getCount());
            }
        }
        Assertions.assertTrue(latch.await(1, TimeUnit.SECONDS));
    }
}
