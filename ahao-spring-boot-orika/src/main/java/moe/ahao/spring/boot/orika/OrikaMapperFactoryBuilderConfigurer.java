package moe.ahao.spring.boot.orika;

import ma.glasnost.orika.impl.DefaultMapperFactory;

public interface OrikaMapperFactoryBuilderConfigurer {
    void configure(DefaultMapperFactory.MapperFactoryBuilder<?, ?> builder);
}
