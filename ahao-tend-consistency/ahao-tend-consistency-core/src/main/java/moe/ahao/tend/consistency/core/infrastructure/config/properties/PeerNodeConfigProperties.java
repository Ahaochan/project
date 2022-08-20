package moe.ahao.tend.consistency.core.infrastructure.config.properties;

import lombok.Data;
import moe.ahao.tend.consistency.core.election.entity.PeerNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务分库相关的配置
 **/
@Data
@ConfigurationProperties(prefix = "ruyuan.tend.consistency.peers")
public class PeerNodeConfigProperties {

    /**
     * 集群节点的配置信息 格式: ip1:port:id1,ip2:port:id2,ip3:port:id3
     */
    public String peersConfig;

    /**
     * 解析配置文件配置的一致性任务集群地址信息为list格式
     *
     * @return 集群地址列表
     */
    @Deprecated
    public List<String> parsePeersConfigToList() {
        if (StringUtils.isEmpty(peersConfig)) {
            return Collections.emptyList();
        }
        return Arrays.stream(StringUtils.split(peersConfig, ','))
            .map(PeerNode::new)
            .map(PeerNode::toString)
            .collect(Collectors.toList());
    }

    /**
     * 解析配置文件配置的一致性任务集群地址信息为list格式
     *
     * @return 集群地址列表
     */
    public List<PeerNode> parsePeerNodes() {
        if (StringUtils.isEmpty(peersConfig)) {
            return Collections.emptyList();
        }
        return Arrays.stream(StringUtils.split(peersConfig, ','))
            .map(PeerNode::new)
            .collect(Collectors.toList());
    }
}
