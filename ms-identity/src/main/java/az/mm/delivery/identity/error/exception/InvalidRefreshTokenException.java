package az.mm.delivery.identity.error.exception;

import az.mm.delivery.common.error.exception.CommonAuthException;

public class InvalidRefreshTokenException extends CommonAuthException {

    private static InvalidRefreshTokenException instance;

    private InvalidRefreshTokenException() {
        super(401, "Invalid refresh token");
    }

    public static InvalidRefreshTokenException getInstance() {
        if (instance == null)
            synchronized (InvalidRefreshTokenException.class) {
                instance = new InvalidRefreshTokenException();
            }
        return instance;
    }

}
