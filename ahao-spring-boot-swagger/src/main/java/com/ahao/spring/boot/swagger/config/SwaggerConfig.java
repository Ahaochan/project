package com.ahao.spring.boot.swagger.config;

import com.ahao.util.commons.lang.CollectionHelper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "swagger.open", havingValue = "true")
public class SwaggerConfig {

    private List<ResponseMessage> defaultGlobalResponseMessage = Arrays.asList(
            new ResponseMessageBuilder().code(200).message("请求成功").responseModel(new ModelRef("String")).build(),
            new ResponseMessageBuilder().code(404).message("链接不存在").responseModel(new ModelRef("String")).build()
    );
    private Set<String> defaultContentType = CollectionHelper.toSet(MediaType.APPLICATION_JSON.getType());

    private Parameter tokenParam = new ParameterBuilder().name("token").description("验证token")
            .parameterType("header").required(false).modelRef(new ModelRef("String")).build();

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
                .globalOperationParameters(Collections.singletonList(tokenParam)) // 全局参数, token
                .globalResponseMessage(RequestMethod.GET,   defaultGlobalResponseMessage)
                .globalResponseMessage(RequestMethod.HEAD,  defaultGlobalResponseMessage)
                .globalResponseMessage(RequestMethod.POST,  defaultGlobalResponseMessage)
                .globalResponseMessage(RequestMethod.PUT,   defaultGlobalResponseMessage)
                .globalResponseMessage(RequestMethod.PATCH, defaultGlobalResponseMessage)

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
                .globalOperationParameters(Collections.singletonList(tokenParam)) // 全局参数, token
                .globalResponseMessage(RequestMethod.GET,   defaultGlobalResponseMessage)
                .globalResponseMessage(RequestMethod.HEAD,  defaultGlobalResponseMessage)
                .globalResponseMessage(RequestMethod.POST,  defaultGlobalResponseMessage)
                .globalResponseMessage(RequestMethod.PUT,   defaultGlobalResponseMessage)
                .globalResponseMessage(RequestMethod.PATCH, defaultGlobalResponseMessage)

                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ahao.spring.boot.swagger.controller"))
                .paths(PathSelectors.any())

                .build();
    }


}
