package moe.ahao.spring.cloud.alibaba.oss.service;

import com.alibaba.alicloud.context.AliCloudProperties;
import com.alibaba.alicloud.context.oss.OssProperties;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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
    private static final Logger logger = LoggerFactory.getLogger(AbstractAlibabaOssService.class);

    private OSS ossClient;
    private OssProperties ossProperties;
    private AliCloudProperties aliCloudProperties;

    private LRUMap<String, String> bucketHostMap = new LRUMap<>();

    @Override
    public String putObject(String key, File value) {
        String bucketName = this.bucketName();
        String bucketHost = this.getBucketHost(bucketName);
        String keyPrefix = this.keyPrefix();

        PutObjectResult result = ossClient.putObject(bucketName, keyPrefix + key, value);
        String url = bucketHost + "/" + key;
        logger.debug("OSS上传key:{} 成功, 获取链接:{}", key, url);
        return url;
    }

    @Override
    public String putObject(String key, InputStream value) {
        String bucketName = this.bucketName();
        String bucketHost = this.getBucketHost(bucketName);
        String keyPrefix = this.keyPrefix();

        PutObjectResult result = ossClient.putObject(bucketName, keyPrefix + key, value);
        String url = bucketHost + "/" + key;
        logger.debug("OSS上传key:{} 成功, 获取链接:{}", key, url);
        return url;
    }

    @Override
    public String getUrl(String key, Date expireTime) {
        String bucketName = this.bucketName();
        String bucketHost = this.getBucketHost(bucketName);
        String keyPrefix = this.keyPrefix();

        URL url = ossClient.generatePresignedUrl(bucketName, keyPrefix + key, expireTime);
        return url.toString();
    }

    protected String getBucketHost(String bucketName) {
        String host = bucketHostMap.get(bucketName);
        if(StringUtils.isBlank(host)) {
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
            }
            ClientBuilderConfiguration config = ObjectUtils.defaultIfNull(ossProperties.getConfig(), new ClientBuilderConfiguration());
            String protocol = config.getProtocol().toString();
            String endpoint = ossProperties.getEndpoint();
            String newHost  = protocol + "://" + bucketName + "." + endpoint;
            bucketHostMap.put(bucketName, newHost);
            host = newHost;
        }
        return host;
    }

    protected abstract String bucketName();
    protected String keyPrefix() {
        return "";
    }

    public OSS getOssClient() {
        return ossClient;
    }

    @Autowired
    public void setOssClient(OSS ossClient) {
        this.ossClient = ossClient;
    }

    public OssProperties getOssProperties() {
        return ossProperties;
    }

    @Autowired
    public void setOssProperties(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    public AliCloudProperties getAliCloudProperties() {
        return aliCloudProperties;
    }

    @Autowired
    public void setAliCloudProperties(AliCloudProperties aliCloudProperties) {
        this.aliCloudProperties = aliCloudProperties;
    }
}
