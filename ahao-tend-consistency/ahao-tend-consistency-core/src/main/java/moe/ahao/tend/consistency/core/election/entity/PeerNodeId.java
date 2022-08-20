package moe.ahao.tend.consistency.core.election.entity;

import lombok.Data;

import java.util.Objects;

@Data
public class PeerNodeId {
    private final Integer val;

    @Override
    public String toString() {
        return val.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeerNodeId that = (PeerNodeId) o;
        return Objects.equals(val, that.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(val);
    }
}
