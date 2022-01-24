package moe.ahao.spring.boot.ddd.domain.entity;

import moe.ahao.spring.boot.ddd.domain.value.OrderIdVal;
import moe.ahao.spring.boot.ddd.domain.value.OrderNoVal;

public class Order implements Entity {
    private OrderIdVal orderId;
    private OrderNoVal orderNo;

    public Order(OrderIdVal orderId) {
        this.orderId = orderId;
    }

    public OrderIdVal getOrderId() {
        return orderId;
    }

    public void setOrderId(OrderIdVal orderId) {
        this.orderId = orderId;
    }

    public OrderNoVal getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(OrderNoVal orderNo) {
        this.orderNo = orderNo;
    }
}
