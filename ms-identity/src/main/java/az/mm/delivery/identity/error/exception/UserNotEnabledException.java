package az.mm.delivery.identity.error.exception;

import az.mm.delivery.common.error.exception.CommonAuthException;

public class UserNotEnabledException extends CommonAuthException {

    public UserNotEnabledException() {
        super(401, "User is not enabled");
    }

}
