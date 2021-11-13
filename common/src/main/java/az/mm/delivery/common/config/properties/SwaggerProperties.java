package az.mm.delivery.common.config.properties;

import az.mm.delivery.common.config.CommonSwaggerConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConditionalOnBean(value = CommonSwaggerConfig.class)
public class SwaggerProperties {

    @Value("${application.swagger.base-package:}")
    private String basePackage;

    @Value("${application.swagger.paths:/.*}")
    private String paths;

    @Value("${application.swagger.apiInfo.version:1.0}")
    private String version;

    @Value("${application.swagger.apiInfo.title:Parcel Delivery System}")
    private String title;

    @Value("${application.swagger.apiInfo.description:Spring Boot REST API for Parcel Delivery System}")
    private String description;

    @Value("${application.swagger.apiInfo.termsOfServiceUrl:}")
    private String termsOfServiceUrl;

    @Value("${application.swagger.apiInfo.license:Apache License Version 2.0}")
    private String license;

    @Value("${application.swagger.apiInfo.licenseUrl:https://www.apache.org/licenses/LICENSE-2.0}")
    private String licenseUrl;

    @Value("${application.swagger.apiInfo.contact.name:Mushfiq Mammadov}")
    private String contactName;

    @Value("${application.swagger.apiInfo.contact.url:https://mycertnotes.com/}")
    private String contactUrl;

    @Value("${application.swagger.apiInfo.contact.email:mushfiqazeri@gmail.com}")
    private String contactEmail;

}
