package az.mm.delivery.parcel;

import az.mm.delivery.common.util.LogUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication(scanBasePackages = "az.mm.delivery")
public class MsDeliveryApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MsDeliveryApplication.class);
        Environment env = app.run(args).getEnvironment();
        LogUtil.logApplicationStartup(env);
    }

}
