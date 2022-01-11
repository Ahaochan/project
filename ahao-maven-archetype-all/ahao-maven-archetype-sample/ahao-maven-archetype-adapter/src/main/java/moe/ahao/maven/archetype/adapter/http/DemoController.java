package moe.ahao.maven.archetype.adapter.http;

import moe.ahao.maven.archetype.api.DemoApi;
import moe.ahao.maven.archetype.api.req.DemoCommand;
import moe.ahao.maven.archetype.api.req.DemoInfoQuery;
import moe.ahao.maven.archetype.api.resp.DemoInfoDTO;
import moe.ahao.maven.archetype.api.resp.Result;
import moe.ahao.maven.archetype.application.DemoCommandService;
import moe.ahao.maven.archetype.application.DemoQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController implements DemoApi {
    @Autowired
    private DemoCommandService demoCommandService;
    @Autowired
    private DemoQueryService demoQueryService;

    @Override
    public Result<DemoInfoDTO> query(DemoInfoQuery req) {
        Integer id = req.getId();
        String name = demoQueryService.getNameById(id);

        DemoInfoDTO resp = new DemoInfoDTO();
        resp.setId(id);
        resp.setName(name);
        return new Result<>(resp);
    }

    @Override
    public Result<Integer> command(DemoCommand req) {
        Integer id = demoCommandService.save(req);
        return new Result<>(id);
    }
}
