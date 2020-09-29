package moe.ahao.spring.boot.session;

import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @see SessionAutoConfiguration.ServletSessionConfiguration#cookieSerializer(org.springframework.boot.autoconfigure.web.ServerProperties)
 * @see org.springframework.session.web.http.SessionRepositoryFilter
 */
@Configuration(proxyBeanMethods = false)
@EnableRedisHttpSession
public class HttpSessionConfig {
}
