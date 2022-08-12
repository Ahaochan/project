package moe.ahao.spring.boot.elastic.elasticsearch;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration(proxyBeanMethods = false)
@EnableElasticsearchRepositories(basePackages = "moe.ahao.spring.boot.elastic.elasticsearch.**.elasticsearch.repository")
public class ElasticsearchConfig {
}
