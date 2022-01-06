package moe.ahao.maven.archetype.domain.entity;

import moe.ahao.maven.archetype.domain.value.DemoId;
import moe.ahao.maven.archetype.domain.value.DemoName;

public class DemoEntity {
    private DemoId id;
    private DemoName name;

    public DemoEntity(DemoId id) {
        this.id = id;
    }

    public DemoId getId() {
        return id;
    }

    public void setId(DemoId id) {
        this.id = id;
    }

    public DemoName getName() {
        return name;
    }

    public void setName(DemoName name) {
        this.name = name;
    }
}
