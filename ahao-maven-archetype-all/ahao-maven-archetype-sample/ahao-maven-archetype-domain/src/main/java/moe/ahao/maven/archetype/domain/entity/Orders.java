package moe.ahao.maven.archetype.domain.entity;

import moe.ahao.maven.archetype.common.domain.entity.Entity;

import java.util.List;

public class Orders implements Entity {
    private List<Order> orders;

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public boolean isNotEmpty() {
        return orders != null && orders.size() > 0;
    }
}
