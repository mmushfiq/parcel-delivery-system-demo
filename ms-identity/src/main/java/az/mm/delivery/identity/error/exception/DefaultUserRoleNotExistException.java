package az.mm.delivery.identity.error.exception;

import az.mm.delivery.common.error.exception.CommonAuthException;

public class DefaultUserRoleNotExistException extends CommonAuthException {

    public DefaultUserRoleNotExistException() {
        super(401, "There is no default role of user");
    }

}
