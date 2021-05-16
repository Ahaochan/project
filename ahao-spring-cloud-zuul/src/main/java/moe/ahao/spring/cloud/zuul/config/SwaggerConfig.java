package moe.ahao.spring.cloud.zuul.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.List;
import java.util.stream.Collectors;

@Primary
@Configuration(proxyBeanMethods = false)
public class SwaggerConfig implements SwaggerResourcesProvider {
    @Autowired
    private RouteLocator routeLocator;

    @Override
    public List<SwaggerResource> get() {
        return routeLocator.getRoutes()
            .stream()
            .map(this::swaggerResource)
            .collect(Collectors.toList());
    }

    private SwaggerResource swaggerResource(Route route) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(route.getId());
        swaggerResource.setLocation(route.getPrefix() + "/v2/api-docs");
        return swaggerResource;
    }
}
