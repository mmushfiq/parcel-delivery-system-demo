package az.mm.delivery.gateway.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.servlet.function.RequestPredicates.GET;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SwaggerConfig {

    private final RouteLocator routeLocator;

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

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return route(GET("/").or(GET("/swagger*")), req ->
                ServerResponse.temporaryRedirect(URI.create("swagger-ui/")).build());
    }

    @Primary
    @Bean
    public SwaggerResourcesProvider swaggerResourceProvider() {
        return this::swaggerResources;
    }

    private List<SwaggerResource> swaggerResources() {
        return routeLocator.getRoutes().stream().map(route -> {
            var resource = new SwaggerResource();
            resource.setName(route.getId());
            resource.setLocation(route.getFullPath().replace("**", "v2/api-docs"));
            resource.setSwaggerVersion(DocumentationType.OAS_30.getVersion());
            return resource;
        }).collect(Collectors.toList());
    }

}
