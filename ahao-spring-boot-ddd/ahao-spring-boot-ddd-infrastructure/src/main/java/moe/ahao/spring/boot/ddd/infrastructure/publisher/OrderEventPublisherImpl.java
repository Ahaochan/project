package moe.ahao.spring.boot.ddd.infrastructure.publisher;

import moe.ahao.spring.boot.ddd.domain.acl.publisher.OrderEventPublisher;
import moe.ahao.spring.boot.ddd.domain.event.OrderCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisherImpl implements OrderEventPublisher {
    @Autowired
    private ApplicationEventPublisher publisher;
    @Override
    public void publish(OrderCreatedEvent event) {
        publisher.publishEvent(event);
    }
}
