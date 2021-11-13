package az.mm.delivery.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public final class LogUtil {

    private LogUtil() {
    }

    public static String getParams(final String[] paramNames, final Object[] paramValues) {
        return IntStream.range(0, paramNames.length)
                .mapToObj(i -> paramNames[i] + "=" + paramValues[i])
                .collect(Collectors.joining(", ", "[", "]"));
    }

    public static Map<String, Object> getParamsAsMap(final String[] paramNames, final Object[] paramValues) {
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < paramNames.length; i++)
            params.put(paramNames[i], paramValues[i]);
        return params;
    }

    public static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null || contextPath.isBlank()) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}{}\n\t" +
                        "External: \t{}://{}:{}{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles());
    }

}
