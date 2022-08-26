package moe.ahao.spring.cloud.ribbon.rule;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.ExtendBalancer;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.Server;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 1. 查询所有健康的实例
 * 2. 如果配置了目标版本, 则过滤符合版本的实例
 * 3. 如果配置了集群名称, 则过滤同集群下的实例
 * 4. 调用 Nacos 自带的权重负载均衡算法
 *
 * @see com.alibaba.cloud.nacos.ribbon.NacosRule
 * @see com.alibaba.cloud.nacos.balancer.NacosBalancer
 * @see <a href="https://github.com/alibaba/spring-cloud-alibaba/issues/2177">spring-cloud-starter-alibaba-nacos-discovery-2021.1.jar不再支持Ribbon?</a>
 */
public class NacosLoadBalancerRule extends AbstractLoadBalancerRule {
    private static final Logger logger = LoggerFactory.getLogger(NacosLoadBalancerRule.class);

    private NacosDiscoveryProperties properties;

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
    }

    @Override
    public Server choose(Object key) {
        try {
            DynamicServerListLoadBalancer<Server> loadBalancer = (DynamicServerListLoadBalancer<Server>) super.getLoadBalancer();
            String name = loadBalancer.getName();

            // 1. 查询所有健康的实例
            NamingService namingService = this.properties.namingServiceInstance();
            List<Instance> instances = namingService.selectInstances(name, true);

            // 2. 如果配置了目标版本, 则过滤符合版本的实例
            instances = this.filterByVersion(instances);

            // 3. 如果配置了集群名称, 则过滤同集群下的实例
            instances = this.filterByCluster(instances);

            // 4. 调用 Nacos 自带的权重负载均衡算法
            Instance instance = ExtendBalancer.getHostByRandomWeight2(instances);
            return new NacosServer(instance);
        } catch (Exception e) {
            logger.warn("发生异常", e);
            return null;
        }
    }

    private List<Instance> filterByVersion(List<Instance> instanceList) {
        if(CollectionUtils.isEmpty(instanceList)) {
            return instanceList;
        }
        String targetVersion = this.properties.getMetadata().get("target-version");
        if(StringUtils.isBlank(targetVersion)) {
            logger.warn("服务未配置target-version! 不进行版本过滤");
            return instanceList;
        }
        List<Instance> filterList = instanceList.stream()
            .filter(instance -> Objects.equals(targetVersion, instance.getMetadata().get("version")))
            .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(filterList)) {
            logger.warn("服务未找到target-version:{}的实例! 请检查配置. 实例列表={}", targetVersion, instanceList);
            return Collections.emptyList();
        }
        return filterList;
    }

    private List<Instance> filterByCluster(List<Instance> instanceList) {
        if(CollectionUtils.isEmpty(instanceList)) {
            return instanceList;
        }
        String clusterName = this.properties.getClusterName();
        if(StringUtils.isBlank(clusterName)) {
            logger.warn("服务未配置clusterName! 不进行集群过滤");
            return instanceList;
        }
        List<Instance> filterList = instanceList.stream()
            .filter(instance -> Objects.equals(clusterName, instance.getClusterName()))
            .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(filterList)) {
            logger.warn("服务未找到clusterName:{}的实例! 发生跨集群调用. 实例列表={}", clusterName, instanceList);
            return instanceList;
        }
        return filterList;
    }

    public void setProperties(NacosDiscoveryProperties properties) {
        this.properties = properties;
    }

}
