package moe.ahao.maven.archetype.api;

import moe.ahao.maven.archetype.api.req.DemoCommand;
import moe.ahao.maven.archetype.api.req.DemoInfoQuery;
import moe.ahao.maven.archetype.api.resp.DemoInfoDTO;
import moe.ahao.maven.archetype.api.resp.Result;
import org.springframework.web.bind.annotation.GetMapping;

public interface DemoApi {
    @GetMapping("/query")
    Result<DemoInfoDTO> query(DemoInfoQuery req);

    @GetMapping("/command")
    Result<Integer> command(DemoCommand req);
}
