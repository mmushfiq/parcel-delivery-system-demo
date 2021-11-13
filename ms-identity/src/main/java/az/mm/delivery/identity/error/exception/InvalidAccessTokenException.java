package az.mm.delivery.identity.error.exception;

import az.mm.delivery.common.error.exception.CommonAuthException;

public class InvalidAccessTokenException extends CommonAuthException {

    public InvalidAccessTokenException() {
        super(401, "Invalid access token");
    }
}
