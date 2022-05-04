package moe.ahao.spring.boot.redis;

import moe.ahao.embedded.EmbeddedRedisTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * https://github.com/redisson/redisson/wiki/%E7%9B%AE%E5%BD%95
 */
class RedissionClientTest extends EmbeddedRedisTest {
    @Test
    void single() {
        Config config = new Config();
        config.useSingleServer()
            .setAddress("redis://127.0.0.1:6379").setPassword(null);
        RedissonClient redisson = Redisson.create(config);

        this.test(redisson);
    }

    @Test
    @Disabled("集群配置脚本参考script目录")
    void cluster() {
        Config config = new Config();
        config.useClusterServers()
            .addNodeAddress("redis://127.0.0.1:7000").addNodeAddress("redis://127.0.0.1:7001")
            .addNodeAddress("redis://127.0.0.1:7002").addNodeAddress("redis://127.0.0.1:7003")
            .addNodeAddress("redis://127.0.0.1:7004").addNodeAddress("redis://127.0.0.1:7005")
            .addNodeAddress("redis://127.0.0.1:7006").addNodeAddress("redis://127.0.0.1:7007");
        RedissonClient redisson = Redisson.create(config);

        this.test(redisson);
    }

    private void test(RedissonClient redisson) {
        RLock lock = redisson.getLock("ahaoLock");
        lock.lock();
        lock.unlock();

        RMap<String, Object> map = redisson.getMap("ahaoMap");
        map.put("ahao", "chan");
        map = redisson.getMap("ahaoMap");
        System.out.println(map.get("ahao"));
    }
}
