package az.mm.delivery.identity.error.exception;

import az.mm.delivery.common.error.exception.CommonAuthException;

public class UserNotFoundException extends CommonAuthException {

    public UserNotFoundException() {
        super(401, "User is not found!");
    }

}
