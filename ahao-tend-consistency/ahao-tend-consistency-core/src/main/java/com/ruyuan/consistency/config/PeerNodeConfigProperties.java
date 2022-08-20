package com.ruyuan.consistency.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 任务分库相关的配置
 *
 * @author zhonghuashishan
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "ruyuan.tend.consistency.peers")
public class PeerNodeConfigProperties {

    /**
     * 集群节点的配置信息 格式: ip1:port:id1,ip2:port:id2,ip3:port:id3
     */
    public String peersConfig;

}
