package moe.ahao.maven.archetype.domain.acl.publisher;

import moe.ahao.maven.archetype.domain.event.OrderCreatedEvent;

public interface OrderEventPublisher {
    void publish(OrderCreatedEvent event);
}
