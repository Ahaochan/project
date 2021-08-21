package moe.ahao.web.module.inventory.service.impl;

import moe.ahao.web.module.inventory.entity.ProductInventory;
import moe.ahao.web.module.inventory.mapper.ProductInventoryMapper;
import moe.ahao.web.module.inventory.service.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProductInventoryServiceImpl implements ProductInventoryService {
    @Autowired
    private ProductInventoryMapper productInventoryMapper;
    private Map<Long, Long> redisCache = new HashMap<>();

    @Override
    public ProductInventory findOneById(Long id) {
        if(id == null || id < 0) {
            return null;
        }
        return productInventoryMapper.selectOneByProductId(id);
    }

    @Override
    public boolean updateByProductId(Long id, Long count) {
        productInventoryMapper.updateCountByproductId(id, count);
        return true;
    }

    @Override
    public boolean saveCache(Long id, Long count) {
        if(id == null || id < 0) {
            return false;
        }
        // String key = this.getCacheKey(id);
        // RedisHelper.setEx(key, count, 1, TimeUnit.HOURS);
        redisCache.put(id, count);
        return true;
    }

    @Override
    public boolean removeCache(Long id) {
        if(id == null || id < 0) {
            return false;
        }
        // String key = this.getCacheKey(id);
        // return RedisHelper.del(key);
        redisCache.put(id, null);
        return true;
    }

    @Override
    public Long getCache(Long id) {
        if(id == null || id < 0) {
            return -1L;
        }
        // String key = this.getCacheKey(id);
        // Long count = RedisHelper.getLong(key);
        Long count = redisCache.get(id);
        return count;
    }

    private String getCacheKey(Long productId) {
        return "product:inventory:" + productId;
    }
}
