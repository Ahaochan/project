package moe.ahao.web.module.inventory.entity;

public class ProductInventory {
    private Long productId;
    private Long productInventoryCount;

    public ProductInventory() {
    }

    public ProductInventory(Long productId, Long inventoryCount) {
        this.productId = productId;
        this.productInventoryCount = inventoryCount;
    }

    public Long getProductId() {
        return productId;
    }

    public ProductInventory setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public Long getProductInventoryCount() {
        return productInventoryCount;
    }

    public ProductInventory setProductInventoryCount(Long productInventoryCount) {
        this.productInventoryCount = productInventoryCount;
        return this;
    }
}
