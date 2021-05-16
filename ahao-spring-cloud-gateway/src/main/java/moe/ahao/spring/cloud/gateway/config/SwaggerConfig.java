package moe.ahao.spring.cloud.gateway.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Primary
@Configuration(proxyBeanMethods = false)
public class SwaggerConfig implements SwaggerResourcesProvider {

    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> swaggerResourceList = new ArrayList<>();
        routeDefinitionLocator.getRouteDefinitions()
            .map(this::swaggerResource)
            .subscribe(swaggerResourceList::add);
        return swaggerResourceList;
    }

    private SwaggerResource swaggerResource(RouteDefinition definition) {
        String location = definition.getPredicates().stream()
            .filter(predicate -> "Path".equalsIgnoreCase(predicate.getName()))
            .findFirst()
            .map(PredicateDefinition::getArgs)
            .map(map -> map.getOrDefault("pattern", map.get(NameUtils.GENERATED_NAME_PREFIX + "0")))
            .map(pattern -> StringUtils.substringBefore(pattern, "*"))
            .map(pattern -> pattern + "v2/api-docs")
            .orElse(null);

        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(definition.getId());
        swaggerResource.setLocation(location);
        return swaggerResource;
    }
}
