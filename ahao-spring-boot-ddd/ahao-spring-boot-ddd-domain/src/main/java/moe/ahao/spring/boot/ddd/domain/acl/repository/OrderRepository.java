package moe.ahao.spring.boot.ddd.domain.acl.repository;

import moe.ahao.spring.boot.ddd.domain.entity.Order;
import moe.ahao.spring.boot.ddd.domain.entity.Orders;
import moe.ahao.spring.boot.ddd.domain.value.OrderIdVal;
import moe.ahao.spring.boot.ddd.domain.value.OrderNoVal;

public interface OrderRepository {
    OrderIdVal save(Order entity);

    Order findOne(OrderIdVal id);

    Orders findSameNo(OrderNoVal orderNo);
}
