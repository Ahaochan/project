package moe.ahao.tend.consistency.core.election.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Data
public final class PeerNode {
    private final String address;
    private final String ip;
    private final int port;
    private final PeerNodeId id;

    public PeerNode(String peerConfig) {
        String[] split = StringUtils.split(peerConfig, ':');
        if (split.length != 3) {
            throw new IllegalArgumentException("PeerNode配置非法: " + peerConfig);
        }
        this.ip = split[0];
        this.port = Integer.parseInt(split[1]);
        this.id = new PeerNodeId(Integer.valueOf(split[2]));
        this.address = ip + ":" + port;
    }

    @Override
    public String toString() {
        return ip + ":" + port + ":" + id.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeerNode peerNode = (PeerNode) o;
        return port == peerNode.port && Objects.equals(address, peerNode.address) && Objects.equals(ip, peerNode.ip) && Objects.equals(id, peerNode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, ip, port, id);
    }
}
