package moe.ahao.maven.archetype.application;

import moe.ahao.maven.archetype.infrastructure.repository.mybatis.mapper.DemoMapper;
import moe.ahao.maven.archetype.infrastructure.repository.mybatis.po.DemoPO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DemoQueryService {
    @Resource
    private DemoMapper demoMapper;

    public String getNameById(Integer id) {
        DemoPO demoPO = demoMapper.selectById(id);
        return demoPO.getName();
    }
}
