package az.mm.delivery.common.error.exception;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private final Integer code;
    private final String message;

    public CommonException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
