package az.mm.delivery.common.error.exception;

import lombok.Getter;

@Getter
public class CommonAuthException extends RuntimeException {

    private final Integer code;
    private final String message;

    public CommonAuthException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}
