package az.mm.delivery.identity.error.exception;

import az.mm.delivery.common.error.exception.CommonBadRequestException;

public class UsernameAlreadyExistsException extends CommonBadRequestException {

    public UsernameAlreadyExistsException() {
        super(400, "Username already exists!");
    }

}
