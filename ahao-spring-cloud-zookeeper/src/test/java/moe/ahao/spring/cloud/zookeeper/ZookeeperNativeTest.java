package moe.ahao.spring.cloud.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class ZookeeperNativeTest {
    public static final String connectString = "192.168.75.133:2181";
    private static final Watcher watcher = e -> System.out.printf("watch事件:%s%n", e);

    @Test
    public void connect() throws Exception {
        long sessionId;
        byte[] password;
        // 1. 连接
        try (ZooKeeper zk = new ZooKeeper(connectString, 5000, watcher, false);) {
            System.out.printf("连接状态:%s%n", zk.getState());
            Thread.sleep(1000);
            System.out.printf("连接状态:%s%n", zk.getState());

            sessionId = zk.getSessionId();
            password = zk.getSessionPasswd();
        }

        // 2. 重连
        try (ZooKeeper zk = new ZooKeeper(connectString, 5000, watcher, sessionId, password, false);) {
            System.out.printf("连接状态:%s%n", zk.getState());
            Thread.sleep(1000);
            System.out.printf("连接状态:%s%n", zk.getState());

            Assertions.assertEquals(sessionId, zk.getSessionId());
            Assertions.assertEquals(password, zk.getSessionPasswd());

        }
    }

    @Test
    public void node() throws Exception {
        try (ZooKeeper zk = new ZooKeeper(connectString, 5000, watcher, false);) {
            List<ACL> aclList = ZooDefs.Ids.OPEN_ACL_UNSAFE;
            // 同步创建一个临时节点
            zk.create("/ahao-tmp", "ahao-data".getBytes(StandardCharsets.UTF_8), aclList, CreateMode.EPHEMERAL);
            // 异步创建一个永久节点
            zk.create("/ahao-per", "ahao-data".getBytes(StandardCharsets.UTF_8), aclList, CreateMode.PERSISTENT,
                (rc, path, ctx, name) -> System.out.printf("rc:%s, path:%s, ctx:%s, name:%s%n", rc, path, ctx, name),
                "创建成功的ctx");
            zk.delete("/ahao-tmp", 0);
            zk.delete("/ahao-per", 0);

            // 设置数据
            Stat stat = new Stat();
            String path = "/ahao-set";
            byte[] oldData = "old-data".getBytes(StandardCharsets.UTF_8);
            byte[] newData = "new-data".getBytes(StandardCharsets.UTF_8);
            zk.create(path, oldData, aclList, CreateMode.EPHEMERAL);
            Assertions.assertArrayEquals(oldData, zk.getData(path, false, stat));

            zk.setData(path, newData, stat.getVersion());
            Assertions.assertArrayEquals(newData, zk.getData(path, false, stat));
            Assertions.assertEquals(1, stat.getVersion());
            zk.delete(path, stat.getVersion());
            Thread.sleep(1000);

            // 获取子节点
            List<String> children = zk.getChildren("/", false);
            for (String child : children) {
                System.out.printf("子节点名称:%s%n", child);
            }

            // 节点是否存在
            Stat exists = zk.exists("/null", false);
            Assertions.assertNull(exists);
        }
    }


    @Test
    public void acl() throws Exception {
        try (ZooKeeper zk = new ZooKeeper(connectString, 5000, watcher, false);) {
            // 1. 创建 world:anyone:crdwa
            // List<ACL> worldAclList = ZooDefs.Ids.OPEN_ACL_UNSAFE;
            List<ACL> worldAclList = Collections.singletonList(new ACL(ZooDefs.Perms.ALL, new Id("world", "anyone")));
            zk.create("/ahao-world", "world-data".getBytes(StandardCharsets.UTF_8), worldAclList, CreateMode.EPHEMERAL);

            // 2. 创建 auth:ahao:pw:crdwa
            String idPassword = "ahao:pw";
            zk.addAuthInfo("digest", idPassword.getBytes(StandardCharsets.UTF_8));
            List<ACL> authAclList = Collections.singletonList(new ACL(ZooDefs.Perms.ALL, new Id("auth", idPassword)));
            zk.create("/ahao-auth", "auth-data".getBytes(StandardCharsets.UTF_8), authAclList, CreateMode.EPHEMERAL);

            // 3. 创建 digest:ahao:pw:crdwa
            String digest = DigestAuthenticationProvider.generateDigest(idPassword);
            List<ACL> digestAclList = Collections.singletonList(new ACL(ZooDefs.Perms.ALL, new Id("digest", digest)));
            zk.create("/ahao-digest", "digest-data".getBytes(StandardCharsets.UTF_8), digestAclList, CreateMode.PERSISTENT);

            // 4. 创建 ip:127.0.0.1:crdwa
            List<ACL> ipAclList = Collections.singletonList(new ACL(ZooDefs.Perms.ALL, new Id("ip", "127.0.0.1")));
            zk.create("/ahao-ip", "ip-data".getBytes(StandardCharsets.UTF_8), ipAclList, CreateMode.PERSISTENT);
        }
    }

    @Test
    public void digest() throws Exception {
        String idPassword = "ahao:pw";
        String digest = DigestAuthenticationProvider.generateDigest(idPassword);
        System.out.println(digest);
        Assertions.assertEquals("ahao:TuktM4tytoXKQxW2ZlFjQ1oOon8=", digest);
    }
}
