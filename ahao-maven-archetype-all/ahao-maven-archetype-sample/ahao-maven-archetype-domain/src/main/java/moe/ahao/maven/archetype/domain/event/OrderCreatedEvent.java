package moe.ahao.maven.archetype.domain.event;

public class OrderCreatedEvent {
    private final Integer id;

    public OrderCreatedEvent(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
