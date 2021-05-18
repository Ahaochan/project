package moe.ahao.web.module.inventory.request;

import moe.ahao.web.module.inventory.entity.ProductInventory;
import moe.ahao.web.module.inventory.service.ProductInventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductInventoryDBUpdateRequest implements Request {
    private final static Logger logger = LoggerFactory.getLogger(ProductInventoryDBUpdateRequest.class);
    private final ProductInventory productInventory;
    private final ProductInventoryService productInventoryService;

    public ProductInventoryDBUpdateRequest(ProductInventory inventory, ProductInventoryService productInventoryService) {
        this.productInventory = inventory;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {
        Long productId = productInventory.getProductId();
        Long count = productInventory.getProductInventoryCount();
        // 1. 删除缓存
        logger.info("数据库更新请求, 删除缓存中的数据, id:{}", productId);
        productInventoryService.removeCache(productId);

        try {
            logger.info("数据库更新请求, 模拟业务处理耗时, id:{}", productId);
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 2. 更新数据库
        logger.info("数据库更新请求, 更新的数据, id:{}, count:{}", productId, count);
        productInventoryService.updateByProductId(productId, count);
    }

    @Override
    public Long getProductId() {
        return productInventory.getProductId();
    }
}
