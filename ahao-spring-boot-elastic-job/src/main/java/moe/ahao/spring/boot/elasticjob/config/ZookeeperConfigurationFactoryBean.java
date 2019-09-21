package moe.ahao.spring.boot.elasticjob.config;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import org.springframework.beans.factory.FactoryBean;

public class ZookeeperConfigurationFactoryBean implements FactoryBean<ZookeeperConfiguration> {
    /**
     * 连接Zookeeper服务器的列表.
     * 包括IP地址和端口号.
     * 多个地址用逗号分隔.
     * 如: host1:2181,host2:2181.
     */
    private String serverLists;
    /**
     * 命名空间
     */
    private String namespace;
    /**
     * 等待重试的间隔时间的初始值.
     * 单位毫秒
     */
    private int baseSleepTimeMilliseconds = 1000;
    /**
     * 等待重试的间隔时间的最大值.
     * 单位毫秒.
     */
    private int maxSleepTimeMilliseconds = 3000;
    /**
     * 最大重试次数
     */
    private int maxRetries = 3;
    /**
     * 会话超时时间.
     * 单位毫秒.
     */
    private int sessionTimeoutMilliseconds = 0;
    /**
     * 连接超时时间.
     * 单位毫秒.
     */
    private int connectionTimeoutMilliseconds = 0;
    /**
     * 连接Zookeeper的权限令牌.
     * 缺省为不需要权限验证.
     */
    private String digest;

    @Override
    public ZookeeperConfiguration getObject() throws Exception {
        ZookeeperConfiguration bean = new ZookeeperConfiguration(serverLists, namespace);
        bean.setBaseSleepTimeMilliseconds(baseSleepTimeMilliseconds);
        bean.setMaxSleepTimeMilliseconds(maxSleepTimeMilliseconds);
        bean.setMaxRetries(maxRetries);
        bean.setSessionTimeoutMilliseconds(sessionTimeoutMilliseconds);
        bean.setConnectionTimeoutMilliseconds(connectionTimeoutMilliseconds);
        bean.setDigest(digest);
        return bean;
    }

    @Override
    public Class<?> getObjectType() {
        return ZookeeperConfiguration.class;
    }

    public void setServerLists(String serverLists) {
        this.serverLists = serverLists;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setBaseSleepTimeMilliseconds(int baseSleepTimeMilliseconds) {
        this.baseSleepTimeMilliseconds = baseSleepTimeMilliseconds;
    }

    public void setMaxSleepTimeMilliseconds(int maxSleepTimeMilliseconds) {
        this.maxSleepTimeMilliseconds = maxSleepTimeMilliseconds;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public void setSessionTimeoutMilliseconds(int sessionTimeoutMilliseconds) {
        this.sessionTimeoutMilliseconds = sessionTimeoutMilliseconds;
    }

    public void setConnectionTimeoutMilliseconds(int connectionTimeoutMilliseconds) {
        this.connectionTimeoutMilliseconds = connectionTimeoutMilliseconds;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }
}
