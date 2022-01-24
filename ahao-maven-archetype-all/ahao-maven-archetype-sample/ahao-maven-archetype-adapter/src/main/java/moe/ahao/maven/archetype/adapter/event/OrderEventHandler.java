package moe.ahao.maven.archetype.adapter.event;

import moe.ahao.maven.archetype.domain.event.OrderCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {
    @EventListener(classes = OrderCreatedEvent.class)
    public void event(OrderCreatedEvent event) {
        Integer id = event.getId();
        System.out.println("id为" + id + "的实体被创建");
    }
}
