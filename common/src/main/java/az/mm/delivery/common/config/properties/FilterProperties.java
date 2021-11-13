package az.mm.delivery.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Configuration
public class FilterProperties {

    @Value("#{'${application.filter.logging.skipped-urls:/}'.split(', ')}")
    private List<String> skippedUrlsForLoggingFilter = new ArrayList<>();

}