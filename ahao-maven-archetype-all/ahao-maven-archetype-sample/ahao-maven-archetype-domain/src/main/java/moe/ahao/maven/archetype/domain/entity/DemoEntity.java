package moe.ahao.maven.archetype.domain.entity;

import moe.ahao.maven.archetype.domain.value.DemoIdVal;
import moe.ahao.maven.archetype.domain.value.DemoNameVal;

public class DemoEntity {
    private DemoIdVal id;
    private DemoNameVal name;

    public DemoEntity(DemoIdVal id) {
        this.id = id;
    }

    public DemoIdVal getId() {
        return id;
    }

    public void setId(DemoIdVal id) {
        this.id = id;
    }

    public DemoNameVal getName() {
        return name;
    }

    public void setName(DemoNameVal name) {
        this.name = name;
    }
}
