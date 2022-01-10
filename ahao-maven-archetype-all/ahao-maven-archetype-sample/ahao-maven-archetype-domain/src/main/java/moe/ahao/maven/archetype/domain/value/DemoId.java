package moe.ahao.maven.archetype.domain.value;

import java.util.Objects;

public class DemoId {
    private final Integer id;
    public DemoId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DemoId demoId = (DemoId) o;
        return Objects.equals(id, demoId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
