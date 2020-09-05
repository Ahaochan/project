package moe.ahao.spring.boot.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class EhCacheTest {
    @Test
    public void test() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("ehcache.xml");
        CacheManager cacheManager = new CacheManager(is);

        String cacheName = "ahao-cache";
        cacheManager.addCacheIfAbsent(cacheName);
        Cache cache = cacheManager.getCache(cacheName);

        String key = "ahao-key";
        String value = "ahao-value";
        cache.put(new Element(key, value));
        Element element = cache.get(key);
        System.out.println("缓存信息: " +element);
        Assertions.assertEquals(key, element.getObjectKey());
        Assertions.assertEquals(value, element.getObjectValue());
    }
}
