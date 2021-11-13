package az.mm.delivery.identity.config.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.security.authentication.jwt")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenProperties {

    private String base64Secret;
    private Long accessTokenValidityInSeconds;
    private Long refreshTokenValidityInSeconds;

}
