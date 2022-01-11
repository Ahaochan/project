package moe.ahao.maven.archetype.domain.gateway.repository;

import moe.ahao.maven.archetype.domain.entity.DemoEntity;
import moe.ahao.maven.archetype.domain.value.DemoIdVal;
import moe.ahao.maven.archetype.domain.value.DemoNameVal;

import java.util.List;

public interface DemoRepository {
    DemoIdVal insert(DemoEntity entity);

    DemoEntity find(DemoIdVal id);

    List<DemoEntity> findList(DemoNameVal demoName);
}
