package moe.ahao.spring.boot.mybatis.tk.config;

import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

@Configuration(proxyBeanMethods = false)
@MapperScan("moe.ahao.spring.boot.**.mapper")
public class MapperConig {
}
