package moe.ahao.spring.cloud.openfeign.config;

import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class OpenFeignConfig {

    @Bean
    @ConditionalOnMissingBean
    public Decoder feignDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters)));
    }
}
