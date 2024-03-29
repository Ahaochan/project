package moe.ahao.spring.boot.ddd.application.service;

import moe.ahao.spring.boot.ddd.infrastructure.repository.mybatis.data.OrderDO;
import moe.ahao.spring.boot.ddd.infrastructure.repository.mybatis.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderQueryService {
    @Resource
    private OrderMapper orderMapper;

    public String getNameById(Integer id) {
        OrderDO order = orderMapper.selectById(id);
        return order.getOrderNo();
    }
}
