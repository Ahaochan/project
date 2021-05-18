package moe.ahao.web.module.inventory.service;

import moe.ahao.web.module.inventory.entity.ProductInventory;

public interface ProductInventoryService {
    ProductInventory findOneById(Long productId);
    boolean updateByProductId(Long productId, Long productInventoryCount);

    boolean saveCache(Long productId, Long productInventoryCount);
    boolean removeCache(Long productId);
    Long getCache(Long productId);
}
