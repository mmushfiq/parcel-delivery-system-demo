package az.mm.delivery.swagger.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application.swagger")
public class SwaggerProperties {

    private String baseUrl;
    private String docsUrl;
    private String version;
    private List<Definition> definitions = new ArrayList<>();

    @Getter
    @Setter
    public static class Definition {
        private String name;
        private String url;
        private String version;
    }

}
