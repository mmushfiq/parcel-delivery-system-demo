package az.mm.delivery.identity.config;

import az.mm.delivery.common.config.properties.FilterProperties;
import az.mm.delivery.common.security.TokenProvider;
import az.mm.delivery.common.security.handler.SecurityProblemHandler;
import az.mm.delivery.common.util.MessageSourceUtil;
import az.mm.delivery.common.util.WebUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TokenProvider.class, SecurityProblemHandler.class, WebUtil.class, MessageSourceUtil.class,
        FilterProperties.class})
public class CommonLibComponents {
}
