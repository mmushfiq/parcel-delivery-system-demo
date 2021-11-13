package az.mm.delivery.order;

import az.mm.delivery.common.util.LogUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication(scanBasePackages = "az.mm.delivery")
public class MsOrderApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MsOrderApplication.class);
        Environment env = app.run(args).getEnvironment();
        LogUtil.logApplicationStartup(env);
    }

}
