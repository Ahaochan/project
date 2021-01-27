package moe.ahao.spring.boot.orika;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@ConditionalOnProperty(name = "orika.enabled", matchIfMissing = true)
@EnableConfigurationProperties(OrikaProperties.class)
@Configuration(proxyBeanMethods = false)
public class OrikaAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public DefaultMapperFactory.Builder orikaMapperFactoryBuilder(OrikaProperties properties,
                                                                  ObjectProvider<OrikaMapperFactoryBuilderConfigurer> configurers) {
        // 1. 创建默认配置的构建器
        DefaultMapperFactory.Builder builder = new DefaultMapperFactory.Builder();

        // 2. 从配置文件中获取配置
        Optional<OrikaProperties> optional = Optional.ofNullable(properties);
        optional.map(OrikaProperties::getUseAutoMapping).ifPresent(builder::useAutoMapping);
        optional.map(OrikaProperties::getUseBuiltinConverters).ifPresent(builder::useBuiltinConverters);
        optional.map(OrikaProperties::getMapNulls).ifPresent(builder::mapNulls);
        optional.map(OrikaProperties::getDumpStateOnException).ifPresent(builder::dumpStateOnException);
        optional.map(OrikaProperties::getFavorExtension).ifPresent(builder::favorExtension);
        optional.map(OrikaProperties::getCaptureFieldContext).ifPresent(builder::captureFieldContext);

        // 3. 自定义配置
        configurers.forEach(s -> s.configure(builder));
        return builder;
    }

    @ConditionalOnMissingBean
    @Bean
    public MapperFactory orikaMapperFactory(DefaultMapperFactory.MapperFactoryBuilder<?, ?> builder,
                                            ObjectProvider<OrikaMapperFactoryConfigurer> configurers) {
        // 1. 从构建器创建工厂实例
        MapperFactory factory = builder.build();
        // 2. 自定义配置
        configurers.forEach(configurer -> configurer.configure(factory));
        return factory;
    }

    @ConditionalOnMissingBean
    @Bean
    public MapperFacade orikaMapperFacade(MapperFactory factory) {
        return factory.getMapperFacade();
    }
}
