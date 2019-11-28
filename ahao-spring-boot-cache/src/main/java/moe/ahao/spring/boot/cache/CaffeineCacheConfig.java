package moe.ahao.spring.boot.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @see org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration
 */
@Configuration
class CaffeineCacheConfig {

	private final CacheProperties cacheProperties;

	CaffeineCacheConfig(CacheProperties cacheProperties) {
		this.cacheProperties = cacheProperties;
	}

	@Bean
	public CaffeineCacheManager caffeineCacheManager(CacheLoader<Object, Object> cacheLoader) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        String specification = this.cacheProperties.getCaffeine().getSpec();
        if (StringUtils.hasText(specification)) {
            // com.github.benmanes.caffeine.cache.CaffeineSpec.configure
            cacheManager.setCacheSpecification(specification);
        }
        cacheManager.setCacheLoader(cacheLoader);

		List<String> cacheNames = this.cacheProperties.getCacheNames();
		if (!CollectionUtils.isEmpty(cacheNames)) {
			cacheManager.setCacheNames(cacheNames);
		}
		return cacheManager;
	}

    @Bean
    public CacheLoader<Object, Object> cacheLoader() {
        return new CacheLoader<Object, Object>() {
            @Override
            public Object load(Object key) throws Exception {
                return null;
            }

            // 重写这个方法将oldValue值返回回去，进而刷新缓存
            @Override
            public Object reload(Object key, Object oldValue) throws Exception {
                return oldValue;
            }
        };
    }
}
