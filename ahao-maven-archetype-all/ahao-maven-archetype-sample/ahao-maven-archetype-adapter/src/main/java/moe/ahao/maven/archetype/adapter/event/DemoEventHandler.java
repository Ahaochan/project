package moe.ahao.maven.archetype.adapter.event;

import moe.ahao.maven.archetype.domain.event.DemoEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DemoEventHandler {
    @EventListener(classes = DemoEvent.class)
    public void event(DemoEvent event) {
        Integer id = event.getId();
        System.out.println("id为" + id + "的实体被创建");
    }
}
