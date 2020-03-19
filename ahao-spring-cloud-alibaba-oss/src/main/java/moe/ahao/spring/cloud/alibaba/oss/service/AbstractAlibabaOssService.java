package moe.ahao.spring.cloud.alibaba.oss.service;

import com.alibaba.alicloud.context.oss.OssProperties;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * 抽象 Oss 服务, 不同 Bucket 继承本类, 并实现 abstract 方法即可.
 */
public abstract class AbstractAlibabaOssService implements InitializingBean, AlibabaOssService {
    private final static Logger logger = LoggerFactory.getLogger(AbstractAlibabaOssService.class);
    public static final int PERM_DAYS = 365 * 10; // 永久链接为 10 年

    private OSS ossClient;
    private OssProperties properties;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            if (!ossClient.doesBucketExist(bucketName())) {
                ossClient.createBucket(bucketName());
            }
        } catch (Exception e) {
            logger.error("Bucket:" + bucketName() + "初始化失败", e);
            System.exit(-1);
        }
    }

    @Override
    public String putObject(String key, File value) {
        PutObjectResult result = ossClient.putObject(bucketName(), key, value);
        String url = getHost() + "/" + key;
        logger.debug("OSS上传key:{} 成功, 获取链接:{}", key, url);
        return url;
    }

    public String putObject(String key, InputStream value) {
        PutObjectResult result = ossClient.putObject(bucketName(), key, value);
        String url = getHost() + "/" + key;
        logger.debug("OSS上传key:{} 成功, 获取链接:{}", key, url);
        return url;
    }

    @Override
    public String getUrl(String key, Date expireTime) {
        URL url = ossClient.generatePresignedUrl(bucketName(), key, expireTime);
        return url.toString();
    }

    @Autowired
    public void setOssClient(OSS ossClient) {
        this.ossClient = ossClient;
    }

    @Autowired
    public void setProperties(OssProperties properties) {
        this.properties = properties;
    }

    protected String getHost() {
        ClientBuilderConfiguration config = ObjectUtils.defaultIfNull(properties.getConfig(), new ClientBuilderConfiguration());
        String protocol = config.getProtocol().toString();
        String endpoint = properties.getEndpoint();
        return protocol + "://" + bucketName() + "." + endpoint;
    }

    protected abstract String bucketName();
}
