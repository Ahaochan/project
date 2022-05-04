package moe.ahao.spring.boot.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("moe.ahao.**.mapper")
public class MybatisConfig {
}
