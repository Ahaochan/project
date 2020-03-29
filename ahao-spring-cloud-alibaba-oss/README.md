# 简介
阿里云`OSS`对象存储整合`Demo`.
一个`bucket`一个`Service`.

# 使用
1. 在`application.yml`中填写阿里云配置.
2. 编写类, 继承自[`AbstractAlibabaOssService`](./src/main/java/moe/ahao/spring/cloud/alibaba/oss/service/AbstractAlibabaOssService.java), 重写`bucketName`.
```java
@Service
public class TestService extends AbstractAlibabaOssService {
    @Override
    public String bucketName() {
        return "my_bucket";
    }
}
```

# 参考文献
- [官方文档](https://github.com/alibaba/spring-cloud-alibaba/tree/master/spring-cloud-alibaba-examples/oss-example)
