package az.mm.delivery.identity.config;

import az.mm.delivery.identity.config.properties.ApplicationProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.SerializationCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    private final ApplicationProperties.Redis props;

    public RedisConfig(ApplicationProperties properties) {
        this.props = properties.getRedis();
    }

    @Bean
    RedissonClient redissonClient() {
        Config config = new Config();
        config.setCodec(new SerializationCodec());
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(props.getAddress());
        singleServerConfig.setConnectionPoolSize(props.getConnectionPoolSize());
        singleServerConfig.setConnectionMinimumIdleSize(props.getConnectionMinimumIdleSize());
        return Redisson.create(config);
    }

}
