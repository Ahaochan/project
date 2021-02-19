package com.ahao.spring.boot.swagger.config;

import com.ahao.util.commons.lang.CollectionHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Configuration(proxyBeanMethods = false)
public class SwaggerConfig {

    private final List<Response> defaultGlobalResponseMessage = Arrays.asList(
            new ResponseBuilder().code("200").description("请求成功").build(),
            new ResponseBuilder().code("404").description("链接不存在").build()
    );
    private final Set<String> defaultContentType = CollectionHelper.toSet(MediaType.APPLICATION_JSON.getType());

    private final RequestParameter tokenParam = new RequestParameterBuilder().name("token").description("验证token")
            .in(ParameterType.HEADER).required(false).query(q -> q.model(m -> m.scalarModel(ScalarType.STRING))).build();

    @Bean
    public Docket globalDocket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("全局项目接口描述文档").version("1.0")
                .description("1. 描述1\n\n" +
                        "2. 描述2\n\n" +
                        "3. 描述3\n\n")
                .termsOfServiceUrl("https://github.com/Ahaochan")
                .contact(new Contact("Ahaochan", "https://github.com/Ahaochan", "123@qq.com"))
                .license("GNU General Public License v3.0")
                .licenseUrl("https://github.com/Ahaochan/project/blob/master/LICENSE")
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                // .groupName("全局文档")
                .apiInfo(apiInfo)
                .pathMapping("/api")   // Api链接前缀
                .produces(defaultContentType) // 响应体(Response content type)
                .consumes(defaultContentType) // 请求体(Parameter content type)
                .globalRequestParameters(Collections.singletonList(tokenParam)) // 全局参数, token
                .globalResponses(HttpMethod.GET,   defaultGlobalResponseMessage)
                .globalResponses(HttpMethod.HEAD,  defaultGlobalResponseMessage)
                .globalResponses(HttpMethod.POST,  defaultGlobalResponseMessage)
                .globalResponses(HttpMethod.PUT,   defaultGlobalResponseMessage)
                .globalResponses(HttpMethod.PATCH, defaultGlobalResponseMessage)

                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ahao"))
                .paths(PathSelectors.any())

                .build();
    }

    @Bean
    public Docket controllerDocket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("Controller接口描述文档").version("1.0")
                .description("1. 描述1\n\n" +
                        "2. 描述2\n\n" +
                        "3. 描述3\n\n")
                .termsOfServiceUrl("https://github.com/Ahaochan")
                .contact(new Contact("Ahaochan", "https://github.com/Ahaochan", "123@qq.com"))
                .license("GNU General Public License v3.0")
                .licenseUrl("https://github.com/Ahaochan/project/blob/master/LICENSE")
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Controller文档")
                .apiInfo(apiInfo)
                .pathMapping("/api")   // Api链接前缀
                .produces(defaultContentType) // 响应体(Response content type)
                .consumes(defaultContentType) // 请求体(Parameter content type)
                .globalRequestParameters(Collections.singletonList(tokenParam)) // 全局参数, token
                .globalResponses(HttpMethod.GET,   defaultGlobalResponseMessage)
                .globalResponses(HttpMethod.HEAD,  defaultGlobalResponseMessage)
                .globalResponses(HttpMethod.POST,  defaultGlobalResponseMessage)
                .globalResponses(HttpMethod.PUT,   defaultGlobalResponseMessage)
                .globalResponses(HttpMethod.PATCH, defaultGlobalResponseMessage)

                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ahao.spring.boot.swagger.controller"))
                .paths(PathSelectors.any())

                .build();
    }
}
