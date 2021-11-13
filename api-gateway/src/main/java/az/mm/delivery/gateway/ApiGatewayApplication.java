package az.mm.delivery.gateway;

import az.mm.delivery.common.filter.JwtTokenFilter;
import az.mm.delivery.common.security.TokenProvider;
import az.mm.delivery.common.util.LogUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableZuulProxy
@ComponentScan(basePackages = {"az.mm.delivery"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                value = {TokenProvider.class, JwtTokenFilter.class})
        })
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ApiGatewayApplication.class);
        Environment env = app.run(args).getEnvironment();
        LogUtil.logApplicationStartup(env);
    }

}
