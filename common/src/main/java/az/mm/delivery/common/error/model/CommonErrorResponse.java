package az.mm.delivery.common.error.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonErrorResponse {

    private final String id;
    private final Integer code;
    private final String message;

    public CommonErrorResponse(String id, Integer code, String message) {
        this.id = id;
        this.code = code;
        this.message = message;
    }

    public CommonErrorResponse(String id, HttpStatus status, String message) {
        this.id = id;
        this.code = status.value();
        this.message = message;
    }

}
