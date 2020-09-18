package moe.ahao.spring.boot.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class GuavaCacheTest {

    private LoadingCache<String, String> cache;

    @BeforeEach
    public void init() {
        cache = CacheBuilder.newBuilder()
            .maximumSize(200) // 设置大小和条目数
            .expireAfterAccess(1, TimeUnit.SECONDS) // 缓存失效时间
            // 清除缓存的监听器, 用于保活
            .removalListener((RemovalListener<String, String>) removalNotification -> System.out.println("缓存数据删除, key:" + removalNotification.getKey() + ", value:" + removalNotification.getValue()))
            // 缓存加载器, 如果key不存在就加载
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    System.out.println("缓存数据加载, key:" + key);
                    return GuavaCacheTest.this.load(key);
                }
            });
    }

    @AfterEach
    public void destroy() {
        cache.invalidateAll();
    }

    @Test
    public void setAndGet() throws Exception {
        int start = RandomUtils.nextInt(0, 10) * 1000;
        for (int i = 0; i < 100; i++) {
            String key = "cache" + i;
            String realValue = String.valueOf(start + i);
            cache.put(key, realValue);

            String cacheValue = cache.get(key);
            System.out.println("key:" + key + ", cacheValue:" + cacheValue);
            Assertions.assertEquals(String.valueOf(start + i), cacheValue);

            cache.invalidate(key);
            String noValue = cache.get(key);
            System.out.println("key:" + key + ", noValue:" + cacheValue);
            Assertions.assertEquals("load_" + key, noValue);
        }
    }

    @Test
    public void timeout() throws Exception {
        String key = "ahao_key";
        cache.put(key, "ahao_value");
        Thread.sleep(2000);
        Assertions.assertEquals(this.load(key), cache.get(key));
    }


    private String load(String key) {
        return "load_" + key;
    }
}
