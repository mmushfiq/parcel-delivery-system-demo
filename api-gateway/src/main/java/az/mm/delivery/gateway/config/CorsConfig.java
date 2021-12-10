package az.mm.delivery.gateway.config;

import az.mm.delivery.gateway.config.properties.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
@EnableWebFlux
@RequiredArgsConstructor
@Slf4j
public class CorsConfig {

    private final ApplicationProperties applicationProperties;

    @Bean
    CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = applicationProperties.getCors();
        if (!CollectionUtils.isEmpty(corsConfig.getAllowedOrigins())) {
            log.debug("Registering CORS filter; " +
                            "\n allowedOrigins: {}, \n allowedHeaders: {}, \n allowedMethods: {}",
                    corsConfig.getAllowedOrigins(), corsConfig.getAllowedHeaders(), corsConfig.getAllowedMethods());
            source.registerCorsConfiguration("/**", corsConfig);
        }
        return new CorsWebFilter(source);
    }

}
