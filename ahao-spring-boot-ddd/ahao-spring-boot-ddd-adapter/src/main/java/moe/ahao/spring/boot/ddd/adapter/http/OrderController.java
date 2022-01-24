package moe.ahao.spring.boot.ddd.adapter.http;

import moe.ahao.spring.boot.ddd.api.OrderApi;
import moe.ahao.spring.boot.ddd.api.command.OrderCreateCommand;
import moe.ahao.spring.boot.ddd.api.query.OrderInfoByIdQuery;
import moe.ahao.spring.boot.ddd.api.resp.OrderInfoDTO;
import moe.ahao.spring.boot.ddd.api.resp.Result;
import moe.ahao.spring.boot.ddd.application.service.OrderCommandService;
import moe.ahao.spring.boot.ddd.application.service.OrderQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController implements OrderApi {
    @Autowired
    private OrderCommandService orderCommandService;
    @Autowired
    private OrderQueryService orderQueryService;

    @Override
    public Result<OrderInfoDTO> query(OrderInfoByIdQuery req) {
        Integer id = req.getId();
        String name = orderQueryService.getNameById(id);

        OrderInfoDTO resp = new OrderInfoDTO();
        resp.setId(id);
        resp.setName(name);
        return new Result<>(resp);
    }

    @Override
    public Result<Integer> create(OrderCreateCommand req) {
        Integer id = orderCommandService.save(req);
        return new Result<>(id);
    }
}
