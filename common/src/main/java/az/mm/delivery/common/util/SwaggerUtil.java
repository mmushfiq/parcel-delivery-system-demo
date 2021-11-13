package az.mm.delivery.common.util;

import az.mm.delivery.common.config.properties.SwaggerProperties;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;

import java.util.Collections;

public final class SwaggerUtil {

    private SwaggerUtil() {
    }

    public static ApiInfo convertToSpringFoxApiInfo(SwaggerProperties props) {
        return new ApiInfo(
                props.getTitle(),
                props.getDescription(),
                props.getVersion(),
                props.getTermsOfServiceUrl(),
                new Contact(props.getContactName(), props.getContactUrl(), props.getContactEmail()),
                props.getLicense(),
                props.getLicenseUrl(),
                Collections.emptyList());
    }

}
