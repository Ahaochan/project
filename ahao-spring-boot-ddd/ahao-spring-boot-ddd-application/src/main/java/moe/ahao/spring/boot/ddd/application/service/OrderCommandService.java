package moe.ahao.spring.boot.ddd.application.service;

import moe.ahao.spring.boot.ddd.api.command.OrderCreateCommand;
import moe.ahao.spring.boot.ddd.application.assembler.OrderAssembler;
import moe.ahao.spring.boot.ddd.domain.entity.Order;
import moe.ahao.spring.boot.ddd.domain.service.OrderDomainService;
import moe.ahao.spring.boot.ddd.domain.value.OrderIdVal;
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
