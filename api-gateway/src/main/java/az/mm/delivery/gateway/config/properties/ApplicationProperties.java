package az.mm.delivery.gateway.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private final CorsConfiguration cors = new CorsConfiguration();

}
