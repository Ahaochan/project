package moe.ahao.maven.archetype.domain.gateway.publisher;

import moe.ahao.maven.archetype.domain.event.DemoSaveEvent;

public interface DemoEventPublisher {
    void publish(DemoSaveEvent event);
}
