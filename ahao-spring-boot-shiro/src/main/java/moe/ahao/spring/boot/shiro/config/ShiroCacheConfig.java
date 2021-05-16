package moe.ahao.spring.boot.shiro.config;

import moe.ahao.spring.boot.redis.RedisTemplateManager;
import moe.ahao.spring.boot.shiro.realm.PasswordRealm;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@ConditionalOnProperty(name = "shiro.enabled", havingValue = "true", matchIfMissing = true)
public class ShiroCacheConfig {

    @Bean
    @Profile("cache-memory")
    public CacheManager memoryCacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    /**
     * 配置 ehcache 缓存
     * Realm   配置了 authenticationCache 和 authorizationCache
     * Session 配置了 shiro-activeSessionCache
     * @see PasswordRealm#afterPropertiesSet()
     * @see ShiroSessionConfig#enterpriseCacheSessionDAO(CacheManager)
     */
    @Bean
    @Profile("cache-ehcache")
    public CacheManager ehCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return cacheManager;
    }

    @Bean
    @Profile("cache-redis")
    public CacheManager cacheManager(IRedisManager redisManager) {
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisManager(redisManager);
        //redis中针对不同用户缓存
        cacheManager.setPrincipalIdFieldName("id");
        //用户权限信息缓存时间
        cacheManager.setExpire(200000);
        return cacheManager;
    }

    @Bean
    @Profile("cache-redis")
    public IRedisManager redisManager(RedisConnectionFactory redisConnectionFactory) {
        return new RedisTemplateManager(redisConnectionFactory);
    }
}
