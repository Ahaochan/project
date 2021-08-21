package moe.ahao.web.module.inventory.request;

import moe.ahao.web.module.inventory.entity.ProductInventory;
import moe.ahao.web.module.inventory.service.ProductInventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductInventoryCacheReloadRequest implements Request {
    private final static Logger logger = LoggerFactory.getLogger(ProductInventoryCacheReloadRequest.class);

    private Long productId;
    private ProductInventoryService productInventoryService;
    private boolean forceRefresh;

    public ProductInventoryCacheReloadRequest(Long productId, ProductInventoryService productInventoryService) {
        this(productId, productInventoryService, false);
    }

    public ProductInventoryCacheReloadRequest(Long productId, ProductInventoryService productInventoryService, boolean forceRefresh) {
        this.productId = productId;
        this.productInventoryService = productInventoryService;
        this.forceRefresh = forceRefresh;
    }

    @Override
    public void process() {
        // 1. 查询库存
        ProductInventory productInventory = productInventoryService.findOneById(productId);
        logger.info("读请求, 查询数据库中的数据, productId:{}, productInventory:{}", productId, productInventory);
        // 2. 更新缓存
        if(productInventory != null) {
            logger.info("读请求, 更新缓存, productId:{}, count:{}", productId, productInventory.getProductInventoryCount());
            productInventoryService.saveCache(productId, productInventory.getProductInventoryCount());
        }
    }

    @Override
    public Long getProductId() {
        return productId;
    }

    public boolean isForceRefresh() {
        return forceRefresh;
    }

    public ProductInventoryCacheReloadRequest setForceRefresh(boolean forceRefresh) {
        this.forceRefresh = forceRefresh;
        return this;
    }
}
