package moe.ahao.spring.boot.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration(proxyBeanMethods = false)
@EntityScan("moe.ahao")
@EnableJpaRepositories("moe.ahao")
public class JPAConfig {
}
