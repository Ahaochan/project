package moe.ahao.maven.archetype.application.service;

import moe.ahao.maven.archetype.api.command.OrderCreateCommand;
import moe.ahao.maven.archetype.application.converter.OrderAssembler;
import moe.ahao.maven.archetype.domain.entity.Order;
import moe.ahao.maven.archetype.domain.service.OrderDomainService;
import moe.ahao.maven.archetype.domain.value.OrderIdVal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderCommandService {
    @Autowired
    private OrderDomainService orderDomainService;

    public Integer save(OrderCreateCommand command) {
        Order entity = OrderAssembler.INSTANCE.toEntity(command);
        OrderIdVal id = orderDomainService.createOrder(entity);
        return id.getId();
    }
}
