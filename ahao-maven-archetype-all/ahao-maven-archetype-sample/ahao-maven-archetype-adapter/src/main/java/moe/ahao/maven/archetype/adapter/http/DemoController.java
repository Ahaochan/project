package moe.ahao.maven.archetype.adapter.http;

import moe.ahao.maven.archetype.api.DemoApi;
import moe.ahao.maven.archetype.api.req.DemoCommandReq;
import moe.ahao.maven.archetype.api.req.DemoQueryReq;
import moe.ahao.maven.archetype.api.resp.DemoCommandResp;
import moe.ahao.maven.archetype.api.resp.DemoQueryResp;
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
    public DemoQueryResp query(DemoQueryReq req) {
        Integer id = req.getId();
        String name = demoQueryService.getNameById(id);

        DemoQueryResp resp = new DemoQueryResp();
        resp.setId(id);
        resp.setName(name);
        return resp;
    }

    @Override
    public DemoCommandResp command(DemoCommandReq req) {
        DemoCommandResp resp = demoCommandService.save(req);
        return resp;
    }
}
