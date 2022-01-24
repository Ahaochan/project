package moe.ahao.spring.boot.ddd.application.assembler;

import moe.ahao.spring.boot.ddd.api.command.OrderCreateCommand;
import moe.ahao.spring.boot.ddd.domain.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderAssembler {
    OrderAssembler INSTANCE = Mappers.getMapper(OrderAssembler.class);

    @Mappings(
        @Mapping(source = "id", target = "orderId.id")
    )
    Order toEntity(OrderCreateCommand command);
}
