package moe.ahao.maven.archetype.domain.acl.repository;

import moe.ahao.maven.archetype.domain.entity.Order;
import moe.ahao.maven.archetype.domain.entity.Orders;
import moe.ahao.maven.archetype.domain.value.OrderIdVal;
import moe.ahao.maven.archetype.domain.value.OrderNoVal;

public interface OrderRepository {
    OrderIdVal save(Order entity);

    Order findOne(OrderIdVal id);

    Orders findSameNo(OrderNoVal orderNo);
}
