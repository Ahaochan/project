package moe.ahao.maven.archetype.domain.value;

import java.util.Objects;

public class DemoNameVal {
    private final String name;
    public DemoNameVal(String name) {
        if(name == null || name.length() <= 0) {
            throw new RuntimeException("Demo实体名称不能为空");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DemoNameVal demoName = (DemoNameVal) o;
        return Objects.equals(name, demoName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
