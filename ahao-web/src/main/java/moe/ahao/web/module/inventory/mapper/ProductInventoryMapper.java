package moe.ahao.web.module.inventory.mapper;

import moe.ahao.web.module.inventory.entity.ProductInventory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ProductInventoryMapper {
    private Map<Long, ProductInventory> db = new HashMap<>();

    public ProductInventory selectOneByProductId(Long productId) {
        if(productId == null || productId < 0) {
            return null;
        }
        return db.get(productId);
    }

    public int updateCountByproductId(Long productId, Long productInventoryCount) {
        ProductInventory productInventory = this.selectOneByProductId(productId);
        if(productInventory == null) {
            productInventory = new ProductInventory();
        }
        productInventory.setProductInventoryCount(productInventoryCount);
        db.put(productId, productInventory);
        return 1;
    }
}
