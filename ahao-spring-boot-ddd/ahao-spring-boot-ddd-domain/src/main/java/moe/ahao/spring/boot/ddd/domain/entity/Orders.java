package moe.ahao.spring.boot.ddd.domain.entity;


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
