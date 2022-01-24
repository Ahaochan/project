package moe.ahao.maven.archetype.infrastructure.publisher;

import moe.ahao.maven.archetype.domain.acl.publisher.OrderEventPublisher;
import moe.ahao.maven.archetype.domain.event.OrderCreatedEvent;
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
