package com.ahao.spring.boot.shiro.config;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "shiro.enabled", havingValue = "true", matchIfMissing = true)
public class ShiroCacheConfig {

    @Bean
    public CacheManager memoryCacheManager() {
        return new MemoryConstrainedCacheManager();
    }

//    @Bean
    public CacheManager ehCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return cacheManager;
    }

}
