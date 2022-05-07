package moe.ahao.spring.cloud.zookeeper.curator;

import moe.ahao.embedded.EmbeddedZookeeperTest;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

abstract class CuratorBaseTest extends EmbeddedZookeeperTest {
    public static final String connectString = "127.0.0.1:2181";

    protected CuratorFramework zk;

    @BeforeEach
    void beforeEach() throws Exception {
        // 1. 重试间隔为1s, 重试5次, 超过10s没连接上就放弃
        RetryPolicy exponentialBackoffRetry = new ExponentialBackoffRetry(1000, 5, 10000);
        // 2. 重试间隔为1s, 重试5次
        RetryPolicy retryNTimes = new RetryNTimes(5, 1000);
        // 3. 重试间隔为1s, 重试1次
        RetryPolicy retryOneTime = new RetryOneTime(1000);
        // 4. 重试间隔为1s, 一直重试
        RetryPolicy retryForever = new RetryForever(1000);
        // 5. 重试间隔为1s, 超时10s没连接上就放弃
        RetryPolicy retryUntilElapsed = new RetryUntilElapsed(10000, 1000);

        List<AuthInfo> authInfoList = Arrays.asList(
            new AuthInfo("digest", "ahao:pw".getBytes(StandardCharsets.UTF_8))
        );
        zk = CuratorFrameworkFactory.builder()
            .connectString(connectString)
            .sessionTimeoutMs(60 * 1000)    // CuratorFrameworkFactory.DEFAULT_SESSION_TIMEOUT_MS
            .connectionTimeoutMs(15 * 1000) // CuratorFrameworkFactory.DEFAULT_CONNECTION_TIMEOUT_MS
            .retryPolicy(retryNTimes)
            .authorization(authInfoList)
            .namespace("ahao-ns")
            .build();
        zk.start();

        CuratorFrameworkState state = zk.getState();
        System.out.println("客户端状态state:" + state);
    }

    @AfterEach
    void afterEach() {
        zk.close();
        CuratorFrameworkState state = zk.getState();
        System.out.println("客户端状态state:" + state);
    }
}
