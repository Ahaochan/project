package moe.ahao.spring.boot.ddd.domain.event;

public class OrderCreatedEvent {
    private final Integer id;

    public OrderCreatedEvent(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
