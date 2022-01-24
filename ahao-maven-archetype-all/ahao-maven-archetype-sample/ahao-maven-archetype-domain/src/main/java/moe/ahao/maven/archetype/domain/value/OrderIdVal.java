package moe.ahao.maven.archetype.domain.value;

import moe.ahao.maven.archetype.common.domain.value.Val;

import java.util.Objects;

public class OrderIdVal implements Val {
    private final Integer id;
    public OrderIdVal(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderIdVal that = (OrderIdVal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
