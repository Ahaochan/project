package moe.ahao.maven.archetype.api;

import moe.ahao.maven.archetype.api.req.DemoCommandReq;
import moe.ahao.maven.archetype.api.req.DemoQueryReq;
import moe.ahao.maven.archetype.api.resp.DemoCommandResp;
import moe.ahao.maven.archetype.api.resp.DemoQueryResp;
import org.springframework.web.bind.annotation.GetMapping;

public interface DemoApi {
    @GetMapping("/query")
    DemoQueryResp query(DemoQueryReq req);

    @GetMapping("/command")
    DemoCommandResp command(DemoCommandReq req);
}
