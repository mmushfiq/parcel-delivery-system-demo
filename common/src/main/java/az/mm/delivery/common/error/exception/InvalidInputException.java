package az.mm.delivery.common.error.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class InvalidInputException extends RuntimeException {

    private final String message;
    private final List<Object> messageArguments;

    public InvalidInputException(String message, List<Object> messageArguments) {
        this.message = message;
        this.messageArguments = messageArguments;
    }

    public static InvalidInputException of(String message, List<Object> messageArguments) {
        return new InvalidInputException(message, messageArguments);
    }

    public static InvalidInputException of(String message) {
        return new InvalidInputException(message, Collections.emptyList());
    }

    public Object[] messageArguments() {
        return messageArguments != null ? messageArguments.toArray() : new Object[0];
    }

}