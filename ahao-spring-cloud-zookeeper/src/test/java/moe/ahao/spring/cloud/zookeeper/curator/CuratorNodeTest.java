package moe.ahao.spring.cloud.zookeeper.curator;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

class CuratorNodeTest extends CuratorBaseTest {
    private static final CuratorWatcher watcher = e -> System.out.printf("watch事件:%s%n", e);

    @Test
    void node() throws Exception {
        List<ACL> aclList = ZooDefs.Ids.OPEN_ACL_UNSAFE;
        // 同步创建一个临时节点
        zk.create()
            .creatingParentsIfNeeded() // 自动创建父节点
            .withMode(CreateMode.EPHEMERAL)
            .withACL(aclList)
            .forPath("/ahao-tmp", "ahao-data".getBytes(StandardCharsets.UTF_8));
        // 重复创建节点抛出异常
        Assertions.assertThrows(KeeperException.NodeExistsException.class, () ->
            zk.create()
                .creatingParentsIfNeeded() // 自动创建父节点
                .withMode(CreateMode.EPHEMERAL)
                .withACL(aclList)
                .forPath("/ahao-tmp", "ahao-data".getBytes(StandardCharsets.UTF_8)));
        // 异步创建一个永久节点
        zk.create()
            .creatingParentsIfNeeded() // 自动创建父节点
            .withMode(CreateMode.PERSISTENT)
            .withACL(aclList)
            .inBackground((client, event) -> System.out.println(event))
            .forPath("/ahao-per", "ahao-data".getBytes(StandardCharsets.UTF_8));
        zk.delete().forPath("/ahao-tmp");
        zk.delete()
            .guaranteed()               // 删除失败后会重试删除
            .deletingChildrenIfNeeded() // 自动删除子节点
            .forPath("/ahao-per");

        // 设置数据
        Stat stat = new Stat();
        String path = "/ahao-set";
        byte[] oldData = "old-data".getBytes(StandardCharsets.UTF_8);
        byte[] newData = "new-data".getBytes(StandardCharsets.UTF_8);
        zk.create().withMode(CreateMode.EPHEMERAL)
            .withACL(aclList)
            .forPath(path, oldData);
        Assertions.assertArrayEquals(oldData, zk.getData().storingStatIn(stat).forPath(path));

        zk.setData().withVersion(stat.getVersion()).forPath(path, newData);
        Assertions.assertArrayEquals(newData, zk.getData().storingStatIn(stat).forPath(path));
        Assertions.assertEquals(1, stat.getVersion());
        zk.delete().withVersion(stat.getVersion()).forPath(path);
        Thread.sleep(1000);

        // 获取子节点
        zk.getChildren().forPath("/")
            .forEach(child -> System.out.printf("子节点名称:%s%n", child));

        // 节点是否存在
        Stat exists = zk.checkExists().forPath("/null");
        Assertions.assertNull(exists);
    }

    @Test
    void acl() throws Exception {
        // 1. 创建 world:anyone:crdwa
        // List<ACL> worldAclList = ZooDefs.Ids.OPEN_ACL_UNSAFE;
        List<ACL> worldAclList = Collections.singletonList(new ACL(ZooDefs.Perms.ALL, new Id("world", "anyone")));
        zk.create()
            .withMode(CreateMode.EPHEMERAL)
            .withACL(worldAclList, true)
            .forPath("/ahao-world", "world-data".getBytes(StandardCharsets.UTF_8));

        // 2. 创建 auth:ahao:pw:crdwa
        String idPassword = "ahao:pw";
        // zk.addAuthInfo("digest", idPassword.getBytes(StandardCharsets.UTF_8));
        List<ACL> authAclList = Collections.singletonList(new ACL(ZooDefs.Perms.ALL, new Id("auth", idPassword)));
        zk.create()
            .withMode(CreateMode.EPHEMERAL)
            .withACL(authAclList, true)
            .forPath("/ahao-auth", "auth-data".getBytes(StandardCharsets.UTF_8));

        // 3. 创建 digest:ahao:pw:crdwa
        String digest = DigestAuthenticationProvider.generateDigest(idPassword);
        List<ACL> digestAclList = Collections.singletonList(new ACL(ZooDefs.Perms.ALL, new Id("digest", digest)));
        zk.create()
            .withMode(CreateMode.EPHEMERAL)
            .withACL(digestAclList, true)
            .forPath("/ahao-digest", "digest-data".getBytes(StandardCharsets.UTF_8));

        // 4. 创建 ip:127.0.0.1:crdwa
        List<ACL> ipAclList = Collections.singletonList(new ACL(ZooDefs.Perms.ALL, new Id("ip", "127.0.0.1")));
        zk.create()
            .withMode(CreateMode.EPHEMERAL)
            .withACL(ipAclList, true)
            .forPath("/ahao-ip", "ip-data".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void watcher() throws Exception {
        List<ACL> aclList = ZooDefs.Ids.OPEN_ACL_UNSAFE;
        String path = "/ahao-watcher";
        zk.create()
            .withMode(CreateMode.EPHEMERAL)
            .withACL(aclList)
            .forPath(path, "ahao-data".getBytes(StandardCharsets.UTF_8));

        zk.getData().usingWatcher(watcher).forPath(path);
        zk.delete().forPath(path);
    }

    @Test
    void nodeCache() throws Exception {
        String path = "/ahao-node-cache";
        try (NodeCache nodeCache = new NodeCache(zk, path, false);) {

            nodeCache.start(true);
            System.out.println("节点初始化数据为:" + nodeCache.getCurrentData());

            nodeCache.getListenable().addListener(() -> {
                String data = new String(nodeCache.getCurrentData().getData(), StandardCharsets.UTF_8);
                System.out.println("节点路径:" + nodeCache.getCurrentData().getPath() + ", 数据:" + data);
            });

            // 注意, 连续操作可能会丢失监听事件, 因为watcher是需要重复注册的
            zk.create()
                .withMode(CreateMode.EPHEMERAL)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath(path, "data".getBytes(StandardCharsets.UTF_8));
            zk.setData().forPath(path, "new-data1".getBytes(StandardCharsets.UTF_8));
            zk.setData().forPath(path, "new-data2".getBytes(StandardCharsets.UTF_8));
        }
    }

    @Test
    void pathChildrenCache() throws Exception {
        String path = "/ahao-children-cache";
        try (PathChildrenCache pathChildrenCache = new PathChildrenCache(zk, path, true);) {

            pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE); // 初始化方式
            System.out.println("节点初始化数据为:" + pathChildrenCache.getCurrentData());

            pathChildrenCache.getListenable().addListener((client, event) -> {
                switch (event.getType()) {
                    case INITIALIZED: System.out.println("子节点初始化完毕"); break;
                    case CHILD_ADDED: System.out.println("添加子节点:" + event.getData().getPath() + ", 数据:" + new String(event.getData().getData(), StandardCharsets.UTF_8)); break;
                    case CHILD_REMOVED: System.out.println("删除子节点:" + event.getData().getPath());
                        break;
                    case CHILD_UPDATED: System.out.println("修改子节点:" + event.getData().getPath() + ", 数据:" + new String(event.getData().getData(), StandardCharsets.UTF_8));
                        break;
                }
            });

            // 注意, 连续操作可能会丢失监听事件, 因为watcher是需要重复注册的
            zk.create().withMode(CreateMode.EPHEMERAL).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath(path + "/child1", "data".getBytes(StandardCharsets.UTF_8));
            zk.create().withMode(CreateMode.EPHEMERAL).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath(path + "/child2", "data".getBytes(StandardCharsets.UTF_8));
            zk.setData().forPath(path + "/child1", "new-data1".getBytes(StandardCharsets.UTF_8));
            zk.setData().forPath(path + "/child2", "new-data2".getBytes(StandardCharsets.UTF_8));
        }
    }
}
