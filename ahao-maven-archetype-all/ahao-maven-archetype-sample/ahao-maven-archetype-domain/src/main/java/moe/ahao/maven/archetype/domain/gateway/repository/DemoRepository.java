package moe.ahao.maven.archetype.domain.gateway.repository;

import moe.ahao.maven.archetype.domain.entity.DemoEntity;
import moe.ahao.maven.archetype.domain.value.DemoId;
import moe.ahao.maven.archetype.domain.value.DemoName;

import java.util.List;

public interface DemoRepository {
    DemoId insertOne(DemoEntity entity);

    DemoEntity findOneById(DemoId id);

    List<DemoEntity> findListByName(DemoName demoName);
}
