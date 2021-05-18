package moe.ahao.web.module.inventory.controller;

import moe.ahao.web.module.inventory.entity.ProductInventory;
import moe.ahao.web.module.inventory.request.ProductInventoryCacheReloadRequest;
import moe.ahao.web.module.inventory.request.ProductInventoryDBUpdateRequest;
import moe.ahao.web.module.inventory.request.Request;
import moe.ahao.web.module.inventory.service.ProductInventoryService;
import moe.ahao.web.module.inventory.service.RequestAsyncProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/async")
public class ProductInventoryController {
    private final static Logger logger = LoggerFactory.getLogger(ProductInventoryController.class);

    @Autowired
    private ProductInventoryService productInventoryService;
    @Autowired
    private RequestAsyncProcessService requestAsyncProcessService;

    @GetMapping("/updateProductInventory")
    public Boolean updateProductInventory(@RequestParam Long productId, @RequestParam Long productInventoryCount) {
        logger.info("接收到写请求, productId:{}, productInventoryCount:{}", productId, productInventoryCount);
        ProductInventory productInventory = new ProductInventory(productId, productInventoryCount);
        Request request = new ProductInventoryDBUpdateRequest(productInventory, productInventoryService);
        requestAsyncProcessService.process(request);
        return true;
    }

    @GetMapping("/getProductInventoryCount")
    public Long getProductInventoryCount(@RequestParam Long productId) throws Exception {
        logger.info("接收到读请求, productId:{}", productId);
        Request request1 = new ProductInventoryCacheReloadRequest(productId, productInventoryService, false);
        requestAsyncProcessService.process(request1);

        long startTime = System.currentTimeMillis();
        long endTime = 0L, waitTime = 0L;
        while (waitTime < 200L) {
            Long productInventoryCount = productInventoryService.getCache(productId);
            if(productInventoryCount != null) {
                logger.info("接收到读请求, 200ms内从缓存中读到数据, productId:{}, count:{}", productId, productInventoryCount);
                return productInventoryCount;
            } else {
                Thread.sleep(20);
                endTime = System.currentTimeMillis();
                waitTime = endTime - startTime;
            }
        }
        logger.info("接收到读请求, 不能在200ms内从缓存中读到数据, productId:{}", productId);
        ProductInventory productInventory = productInventoryService.findOneById(productId);
        if(productInventory != null) {
            // 1. Redis缓存过期, flag=false, 重新刷新缓存
            // 2. 等待超时, 直接查库, 重新刷新缓存
            // 3. 数据库里本身就没有
            Request request2 = new ProductInventoryCacheReloadRequest(productId, productInventoryService, true);
            logger.info("接收到读请求, 不能在200ms内从缓存中读到数据, 数据库查到数据并刷新缓存, productId:{}, count:{}", productId, productInventory);
            requestAsyncProcessService.process(request2);
            return productInventory.getProductInventoryCount();
        }
        logger.info("接收到读请求, 不能在200ms内从缓存中读到数据, 数据库也查不到数据, productId:{}", productId);
        return null;
    }


}
