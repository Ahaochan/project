package moe.ahao.maven.archetype.adapter.http;

import moe.ahao.maven.archetype.api.OrderApi;
import moe.ahao.maven.archetype.api.command.OrderCreateCommand;
import moe.ahao.maven.archetype.api.query.OrderInfoByIdQuery;
import moe.ahao.maven.archetype.api.resp.OrderInfoDTO;
import moe.ahao.maven.archetype.api.resp.Result;
import moe.ahao.maven.archetype.application.service.OrderCommandService;
import moe.ahao.maven.archetype.application.service.OrderQueryService;
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
