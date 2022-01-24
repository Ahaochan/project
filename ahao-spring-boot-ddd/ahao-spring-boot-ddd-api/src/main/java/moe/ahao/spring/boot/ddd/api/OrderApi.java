package moe.ahao.spring.boot.ddd.api;

import moe.ahao.spring.boot.ddd.api.command.OrderCreateCommand;
import moe.ahao.spring.boot.ddd.api.query.OrderInfoByIdQuery;
import moe.ahao.spring.boot.ddd.api.resp.OrderInfoDTO;
import moe.ahao.spring.boot.ddd.api.resp.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public interface OrderApi {
    @GetMapping("/order/query")
    Result<OrderInfoDTO> query(OrderInfoByIdQuery req);

    @PostMapping("/order/create")
    Result<Integer> create(OrderCreateCommand req);
}
