package moe.ahao.maven.archetype.domain.value;

import java.util.Objects;

public class DemoIdVal {
    private final Integer id;
    public DemoIdVal(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DemoIdVal demoId = (DemoIdVal) o;
        return Objects.equals(id, demoId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
