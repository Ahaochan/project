package moe.ahao.spring.boot.validator.config;

import com.ahao.aop.RequestMappingLogAOP;
import com.ahao.aop.RequestMappingValidatorAOP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AOPConfig {
    @Bean
    public RequestMappingValidatorAOP requestMappingValidatorAOP() {
        return new RequestMappingValidatorAOP();
    }
    @Bean
    public RequestMappingLogAOP requestMappingLogAOP() {
        return new RequestMappingLogAOP();
    }
}
