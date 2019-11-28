package moe.ahao.spring.boot.cache;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.List;

/**
 * 推荐使用 {@link org.springframework.cache.caffeine.CaffeineCacheManager} 和 {@link RedisCacheManager} 做二级缓存
 * @see org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration
 * @see org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration
 * @see org.springframework.boot.autoconfigure.cache.EhCacheCacheConfiguration
 * @see org.springframework.boot.autoconfigure.cache.JCacheCacheConfiguration
 * @see org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
 * @see org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
@EnableCaching
public class CacheConfig {

    @Bean
    @Primary
    public CompositeCacheManager compositeCacheManager(List<CacheManager> cacheManagers) {
        CompositeCacheManager cacheManager = new CompositeCacheManager();
        cacheManager.setCacheManagers(cacheManagers);
        cacheManager.setFallbackToNoOpCache(false);
        return cacheManager;
    }
}
