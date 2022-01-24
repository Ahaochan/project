package moe.ahao.spring.boot.ddd.domain.acl.publisher;

import moe.ahao.spring.boot.ddd.domain.event.OrderCreatedEvent;

public interface OrderEventPublisher {
    void publish(OrderCreatedEvent event);
}
