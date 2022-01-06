package moe.ahao.maven.archetype.application;

import moe.ahao.maven.archetype.api.req.DemoCommandReq;
import moe.ahao.maven.archetype.api.resp.DemoCommandResp;
import moe.ahao.maven.archetype.domain.entity.DemoEntity;
import moe.ahao.maven.archetype.domain.service.DemoDomainService;
import moe.ahao.maven.archetype.domain.value.DemoId;
import moe.ahao.maven.archetype.domain.value.DemoName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoCommandService {
    @Autowired
    private DemoDomainService demoDomainService;

    public DemoCommandResp save(DemoCommandReq req) {
        DemoEntity entity = new DemoEntity(new DemoId(req.getId()));
        entity.setName(new DemoName(req.getName()));

        DemoId id = demoDomainService.save(entity);

        DemoCommandResp resp = new DemoCommandResp();
        resp.setId(id.getId());
        return resp;
    }
}
