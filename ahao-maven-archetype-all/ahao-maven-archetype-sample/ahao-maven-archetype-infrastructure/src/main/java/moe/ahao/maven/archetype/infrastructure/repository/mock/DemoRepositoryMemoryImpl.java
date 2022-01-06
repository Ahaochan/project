package moe.ahao.maven.archetype.infrastructure.repository.mock;

import moe.ahao.maven.archetype.domain.entity.DemoEntity;
import moe.ahao.maven.archetype.domain.gateway.repository.DemoRepository;
import moe.ahao.maven.archetype.domain.value.DemoId;
import moe.ahao.maven.archetype.domain.value.DemoName;
import moe.ahao.maven.archetype.infrastructure.repository.mybatis.po.DemoPO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DemoRepositoryMemoryImpl implements DemoRepository {
    private int increment = 1;
    private Map<Integer, DemoPO> map = new HashMap<>();

    @Override
    public DemoId insertOne(DemoEntity entity) {
        DemoId id = entity.getId();
        if (id == null) {
            id = new DemoId(increment++);
        }

        DemoPO po = new DemoPO();
        po.setId(id.getId());
        po.setName(entity.getName().getName());

        map.put(id.getId(), po);

        id.setId(po.getId());
        return id;
    }

    @Override
    public DemoEntity findOneById(DemoId id) {
        DemoPO po = map.get(id.getId());

        DemoEntity entity = new DemoEntity(new DemoId(po.getId()));
        entity.setName(new DemoName(po.getName()));
        return entity;
    }

    @Override
    public List<DemoEntity> findListByName(DemoName demoName) {
        return map.values().stream()
            .filter(po -> Objects.equals(po.getName(), demoName.getName()))
            .map(po -> {
                DemoEntity entity = new DemoEntity(new DemoId(po.getId()));
                entity.setName(new DemoName(po.getName()));
                return entity;
            })
            .collect(Collectors.toList());
    }
}
