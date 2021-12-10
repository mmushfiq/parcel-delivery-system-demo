package az.mm.delivery.gateway.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StopWatch;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SwaggerConfig {

    private final GatewayProperties gatewayProperties;

    @Bean
    public Docket docket() {
        log.debug("Starting Swagger");
        StopWatch watch = new StopWatch();
        watch.start();

        Docket docket = new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.none())
                .paths(PathSelectors.none())
                .build();

        watch.stop();
        log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return docket;
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .displayRequestDuration(true)
                .build();
    }

    @Primary
    @Bean
    @Lazy
    public SwaggerResourcesProvider swaggerResourcesProvider() {
        return this::swaggerResources;
    }

    private List<SwaggerResource> swaggerResources() {
        return gatewayProperties.getRoutes().stream().map(route -> {
            var resource = new SwaggerResource();
            resource.setName(route.getId());
            resource.setLocation(getRouteLocation(route));
            resource.setSwaggerVersion(DocumentationType.OAS_30.getVersion());
            return resource;
        }).collect(Collectors.toList());
    }

    private String getRouteLocation(RouteDefinition route) {
        return Optional.ofNullable(route.getPredicates().get(0).getArgs().values().toArray()[0])
                .map(String::valueOf)
                .map(s -> s.replace("**", "v2/api-docs"))
                .orElse(null);
    }

}
