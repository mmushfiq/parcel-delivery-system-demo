package az.mm.delivery.identity.annotations;


import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@ExtendWith(EnabledIfRedisReachableCondition.class)
public @interface EnabledIfRedisReachable {

    String host();

    int port();

}