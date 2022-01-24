package moe.ahao.spring.boot.ddd.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import moe.ahao.spring.boot.ddd.domain.acl.repository.OrderRepository;
import moe.ahao.spring.boot.ddd.domain.entity.Order;
import moe.ahao.spring.boot.ddd.domain.entity.Orders;
import moe.ahao.spring.boot.ddd.domain.value.OrderIdVal;
import moe.ahao.spring.boot.ddd.domain.value.OrderNoVal;
import moe.ahao.spring.boot.ddd.infrastructure.converter.OrderConverter;
import moe.ahao.spring.boot.ddd.infrastructure.repository.mybatis.data.OrderDO;
import moe.ahao.spring.boot.ddd.infrastructure.repository.mybatis.mapper.OrderMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class OrderRepositoryMyBatisImpl implements OrderRepository {
    @Resource
    private OrderMapper orderMapper;

    @Override
    public OrderIdVal save(Order entity) {
        OrderIdVal id = entity.getOrderId();

        OrderDO po = new OrderDO();
        po.setOrderNo(entity.getOrderNo().getNo());

        orderMapper.insert(po);

        entity.setOrderId(new OrderIdVal(po.getId()));
        return id;
    }

    @Override
    public Order findOne(OrderIdVal id) {
        OrderDO order = orderMapper.selectById(id.getId());

        Order entity = new Order(id);
        entity.setOrderNo(new OrderNoVal(order.getOrderNo()));
        return entity;
    }

    @Override
    public Orders findSameNo(OrderNoVal orderNo) {
        List<OrderDO> list = orderMapper.selectList(new QueryWrapper<OrderDO>().lambda()
            .eq(OrderDO::getOrderNo, orderNo.getNo()));

        Orders orders = OrderConverter.INSTANCE.toEntities(list);
        return orders;
    }
}
