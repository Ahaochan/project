package moe.ahao.maven.archetype.infrastructure.publisher;

import moe.ahao.maven.archetype.domain.event.DemoSaveEvent;
import moe.ahao.maven.archetype.domain.gateway.publisher.DemoEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DemoEventPublisherImpl implements DemoEventPublisher {
    @Autowired
    private ApplicationEventPublisher publisher;
    @Override
    public void publish(DemoSaveEvent event) {
        publisher.publishEvent(event);
    }
}
