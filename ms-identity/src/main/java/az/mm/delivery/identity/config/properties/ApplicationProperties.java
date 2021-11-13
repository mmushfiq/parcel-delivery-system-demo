package az.mm.delivery.identity.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private final Redis redis = new Redis();

    @Getter
    @Setter
    public static class Redis {
        private String address;
        private int connectionPoolSize;
        private int connectionMinimumIdleSize;
        private String prefix;
        private String tokenPrefix;
        private long tokenTimeToLive;
    }

}