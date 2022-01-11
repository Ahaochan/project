package moe.ahao.maven.archetype.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import moe.ahao.maven.archetype.domain.entity.DemoEntity;
import moe.ahao.maven.archetype.domain.gateway.repository.DemoRepository;
import moe.ahao.maven.archetype.domain.value.DemoIdVal;
import moe.ahao.maven.archetype.domain.value.DemoNameVal;
import moe.ahao.maven.archetype.infrastructure.repository.mybatis.mapper.DemoMapper;
import moe.ahao.maven.archetype.infrastructure.repository.mybatis.po.DemoPO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DemoRepositoryMyBatisImpl implements DemoRepository {
    @Resource
    private DemoMapper demoMapper;

    @Override
    public DemoIdVal insert(DemoEntity entity) {
        DemoIdVal id = entity.getId();

        DemoPO po = new DemoPO();
        po.setName(entity.getName().getName());

        demoMapper.insert(po);

        entity.setId(new DemoIdVal(po.getId()));
        return id;
    }

    @Override
    public DemoEntity find(DemoIdVal id) {
        DemoPO demoPO = demoMapper.selectById(id.getId());

        DemoEntity entity = new DemoEntity(id);
        entity.setName(new DemoNameVal(demoPO.getName()));
        return entity;
    }

    @Override
    public List<DemoEntity> findList(DemoNameVal demoName) {
        List<DemoPO> list = demoMapper.selectList(new QueryWrapper<DemoPO>().lambda()
            .eq(DemoPO::getName, demoName.getName()));

        List<DemoEntity> resultList = new ArrayList<>(list.size());
        for (DemoPO po : list) {
            DemoEntity entity = new DemoEntity(new DemoIdVal(po.getId()));
            entity.setName(new DemoNameVal(po.getName()));

            resultList.add(entity);
        }
        return resultList;
    }
}
