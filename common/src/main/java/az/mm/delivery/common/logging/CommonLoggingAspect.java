package az.mm.delivery.common.logging;

import az.mm.delivery.common.util.LogUtil;
import az.mm.delivery.common.util.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.v;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class CommonLoggingAspect {

    private final WebUtil webUtil;

    @Around("execution(* az.mm.delivery.*.controller..*(..)))")
    public Object logEndpoints(ProceedingJoinPoint joinPoint) throws Throwable {
        String uri = webUtil.getRequestUri();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        Map<String, Object> params = LogUtil.getParamsAsMap(methodSignature.getParameterNames(), joinPoint.getArgs());

        log.debug("[Request]  | Uri: {} [{}.{}] | Params: {}",
                v("uri", uri), className, methodName, v("params", params));

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsedTime = System.currentTimeMillis() - start;

        log.debug("[Response] | Uri: {} [{}.{}] | Elapsed time: {} ms | Result: {}",
                uri, className, methodName, v("elapsed_time", elapsedTime), result);

        return result;
    }

}


