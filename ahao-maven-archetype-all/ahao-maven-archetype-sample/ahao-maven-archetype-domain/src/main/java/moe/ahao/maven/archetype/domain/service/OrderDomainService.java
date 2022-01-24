package moe.ahao.maven.archetype.domain.service;

import moe.ahao.maven.archetype.domain.acl.publisher.OrderEventPublisher;
import moe.ahao.maven.archetype.domain.acl.repository.OrderRepository;
import moe.ahao.maven.archetype.domain.entity.Order;
import moe.ahao.maven.archetype.domain.entity.Orders;
import moe.ahao.maven.archetype.domain.event.OrderCreatedEvent;
import moe.ahao.maven.archetype.domain.exception.OrderExceptionEnum;
import moe.ahao.maven.archetype.domain.value.OrderIdVal;
import moe.ahao.maven.archetype.domain.value.OrderNoVal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDomainService {
    @Autowired
    private OrderEventPublisher publisher;

    @Autowired
    private OrderRepository orderRepository;

    public OrderIdVal createOrder(Order entity) {
        OrderNoVal orderNo = entity.getOrderNo();
        Orders sameNoOrders = orderRepository.findSameNo(orderNo);
        if(sameNoOrders.isNotEmpty()) {
            throw OrderExceptionEnum.ORDER_NO_DUPLICATED.msg(orderNo.getNo());
        }
        OrderIdVal orderId = orderRepository.save(entity);

        OrderCreatedEvent event = new OrderCreatedEvent(orderId.getId());
        publisher.publish(event);
        return orderId;
    }

    public Order findById(OrderIdVal orderId) {
        return orderRepository.findOne(orderId);
    }
}
