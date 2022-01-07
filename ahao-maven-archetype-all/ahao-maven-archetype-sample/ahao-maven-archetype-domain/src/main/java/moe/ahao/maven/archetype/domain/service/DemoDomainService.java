package moe.ahao.maven.archetype.domain.service;

import moe.ahao.maven.archetype.domain.entity.DemoEntity;
import moe.ahao.maven.archetype.domain.event.DemoSaveEvent;
import moe.ahao.maven.archetype.domain.gateway.publisher.DemoEventPublisher;
import moe.ahao.maven.archetype.domain.gateway.repository.DemoRepository;
import moe.ahao.maven.archetype.domain.value.DemoId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoDomainService {
    @Autowired
    private DemoEventPublisher publisher;

    @Autowired
    private DemoRepository demoRepository;

    public DemoId save(DemoEntity entity) {
        List<DemoEntity> sameNameList = demoRepository.findList(entity.getName());
        if(sameNameList.size() > 0) {
            throw new RuntimeException("不能有重名的实体");
        }
        DemoId demoId = demoRepository.insertOne(entity);

        DemoSaveEvent event = new DemoSaveEvent();
        event.setId(demoId.getId());
        publisher.publish(event);
        return demoId;
    }

    public DemoEntity findById(DemoId demoId) {
        return demoRepository.findOne(demoId);
    }
}
