package moe.ahao.spring.boot.ddd.infrastructure.converter;

import moe.ahao.spring.boot.ddd.domain.entity.Order;
import moe.ahao.spring.boot.ddd.domain.entity.Orders;
import moe.ahao.spring.boot.ddd.infrastructure.repository.mybatis.data.OrderDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderConverter {
    OrderConverter INSTANCE = Mappers.getMapper(OrderConverter.class);

    @Mappings(
        @Mapping(source = "id", target = "orderId.id")
    )
    OrderDO toData(Order entity);

    @Mappings(
        @Mapping(source = "id", target = "orderId.id")
    )
    Orders toEntities(List<OrderDO> dataList);
}
