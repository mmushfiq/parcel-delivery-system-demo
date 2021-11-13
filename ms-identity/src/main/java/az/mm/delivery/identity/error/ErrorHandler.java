package az.mm.delivery.identity.error;

import az.mm.delivery.common.error.CommonErrorHandler;
import az.mm.delivery.common.error.model.CommonErrorResponse;
import az.mm.delivery.common.util.WebUtil;
import az.mm.delivery.identity.error.exception.UsernameAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler extends CommonErrorHandler {

    @Autowired
    private WebUtil webUtil;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public CommonErrorResponse handleBadCredentialsException(UsernameAlreadyExistsException ex) {
        addErrorLog(HttpStatus.BAD_REQUEST, ex.getMessage(), "UsernameAlreadyExistsException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public CommonErrorResponse handleBadCredentialsException(BadCredentialsException ex) {
        String message = "Username or password is wrong";
        addErrorLog(HttpStatus.BAD_REQUEST, ex.getMessage(), "BadCredentialsException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                HttpStatus.BAD_REQUEST,
                message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public CommonErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        addErrorLog(HttpStatus.BAD_REQUEST, ex.getMostSpecificCause().getMessage(), ex);
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                HttpStatus.BAD_REQUEST,
                ex.getMostSpecificCause().getMessage());
    }

}
