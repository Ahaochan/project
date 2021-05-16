package moe.ahao.web;

import moe.ahao.spring.bean.PackageBeanNameGenerator;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Web程序启动类
 * @author Ahaochan
 */
public class AhaoServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AhaoApplication.class)
                .beanNameGenerator(new PackageBeanNameGenerator());
    }
}
