package moe.ahao.spring.boot.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TestCacheService {

    public static final String CACHE_KEY = "test-cache";

    /**
     * 获取信息, 第二次访问会取缓存
     */
    @Cacheable(cacheNames = CACHE_KEY)
    public String execute(String id) {
        System.out.println("读取数据"+id);
        return doExecute(id);
    }

    /**
     * 更新缓存
     */
    @CachePut(cacheNames = CACHE_KEY, key = "#id")
    public String updateCache(String id) {
        System.out.println("更新缓存"+id);
        return doExecute(id);
    }

    /**
     * 清除缓存
     */
    @CacheEvict(cacheNames = CACHE_KEY, key = "#id")
    public void removeCache(String id) {
        System.out.println("删除缓存"+id);
    }


    /**
     * 获取string 模拟调用方法
     */
    public String doExecute(String id) {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "My User"+id+", time:" + System.currentTimeMillis();
    }


}
