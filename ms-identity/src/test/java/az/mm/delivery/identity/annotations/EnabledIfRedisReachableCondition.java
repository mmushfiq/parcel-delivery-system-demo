package az.mm.delivery.identity.annotations;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.net.InetSocketAddress;
import java.net.Socket;

import static java.lang.String.format;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.disabled;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.enabled;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

class EnabledIfRedisReachableCondition implements ExecutionCondition {

    private static final ConditionEvaluationResult ENABLED_BY_DEFAULT = enabled("@EnabledIfRedisReachable is not present");

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        AnnotatedElement element = context.getElement()
                .orElseThrow(IllegalStateException::new);
        return findAnnotation(element, EnabledIfRedisReachable.class)
                .map(annotation -> disableIfRedisUnreachable(annotation, element))
                .orElse(ENABLED_BY_DEFAULT);
    }

    private ConditionEvaluationResult disableIfRedisUnreachable(EnabledIfRedisReachable annotation, AnnotatedElement element) {
        String host = annotation.host();
        int port = annotation.port();
        int timeout = 1000;
        boolean reachable = pingHost(host, port, timeout);
        if (reachable)
            return enabled(format("%s is enabled because %s:%d is reachable", element, host, port));
        else
            return disabled(format("%s is disabled because %s:%d could not be reached in %dms", element, host, port, timeout));
    }

    private boolean pingHost(String host, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}