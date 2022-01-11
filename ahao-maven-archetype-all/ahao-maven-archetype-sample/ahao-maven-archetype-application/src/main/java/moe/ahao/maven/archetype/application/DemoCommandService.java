package moe.ahao.maven.archetype.application;

import moe.ahao.maven.archetype.api.req.DemoCommand;
import moe.ahao.maven.archetype.domain.entity.DemoEntity;
import moe.ahao.maven.archetype.domain.service.DemoDomainService;
import moe.ahao.maven.archetype.domain.value.DemoIdVal;
import moe.ahao.maven.archetype.domain.value.DemoNameVal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoCommandService {
    @Autowired
    private DemoDomainService demoDomainService;

    public Integer save(DemoCommand req) {
        DemoEntity entity = new DemoEntity(new DemoIdVal(req.getId()));
        entity.setName(new DemoNameVal(req.getName()));

        DemoIdVal id = demoDomainService.save(entity);

        return id.getId();
    }
}
