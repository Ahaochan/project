package moe.ahao.maven.archetype.adapter.event;

import moe.ahao.maven.archetype.domain.event.DemoSaveEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DemoEventHandler {
    @EventListener(classes = DemoSaveEvent.class)
    public void event(DemoSaveEvent event) {
        Integer id = event.getId();
        System.out.println("id为" + id + "的实体被创建");
    }
}
