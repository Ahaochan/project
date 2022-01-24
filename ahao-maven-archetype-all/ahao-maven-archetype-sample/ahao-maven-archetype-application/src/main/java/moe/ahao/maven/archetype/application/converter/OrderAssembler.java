package moe.ahao.maven.archetype.application.converter;

import moe.ahao.maven.archetype.api.command.OrderCreateCommand;
import moe.ahao.maven.archetype.domain.entity.Order;
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
