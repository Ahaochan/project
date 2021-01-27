package moe.ahao.spring.boot.orika;

import ma.glasnost.orika.MapperFactory;

public interface OrikaMapperFactoryConfigurer {
    void configure(MapperFactory factory);
}
