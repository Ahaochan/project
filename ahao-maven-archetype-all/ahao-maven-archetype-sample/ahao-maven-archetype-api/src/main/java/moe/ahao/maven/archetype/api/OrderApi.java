package moe.ahao.maven.archetype.api;

import moe.ahao.maven.archetype.api.command.OrderCreateCommand;
import moe.ahao.maven.archetype.api.query.OrderInfoByIdQuery;
import moe.ahao.maven.archetype.api.resp.OrderInfoDTO;
import moe.ahao.maven.archetype.api.resp.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public interface OrderApi {
    @GetMapping("/order/query")
    Result<OrderInfoDTO> query(OrderInfoByIdQuery req);

    @PostMapping("/order/create")
    Result<Integer> create(OrderCreateCommand req);
}
