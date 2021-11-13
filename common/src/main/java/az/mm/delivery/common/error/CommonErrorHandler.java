package az.mm.delivery.common.error;

import az.mm.delivery.common.error.exception.CommonAuthException;
import az.mm.delivery.common.error.exception.CommonBadRequestException;
import az.mm.delivery.common.error.exception.CommonException;
import az.mm.delivery.common.error.exception.InvalidInputException;
import az.mm.delivery.common.error.model.CommonErrorResponse;
import az.mm.delivery.common.util.MessageSourceUtil;
import az.mm.delivery.common.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;
import static net.logstash.logback.argument.StructuredArguments.v;

@RestControllerAdvice
@Slf4j
public class CommonErrorHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private WebUtil webUtil;

    @Autowired
    private MessageSourceUtil messageSourceUtil;

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public CommonErrorResponse handleAuthenticationException(AuthenticationException ex) {
        addErrorLog(HttpStatus.UNAUTHORIZED, ex.getMessage(), "AuthenticationException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                HttpStatus.UNAUTHORIZED,
                ex.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public CommonErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
        addErrorLog(HttpStatus.FORBIDDEN, ex.getMessage(), "AccessDeniedException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                HttpStatus.FORBIDDEN,
                ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CommonAuthException.class)
    public CommonErrorResponse handleCommonAuthException(CommonAuthException ex) {
        addErrorLog(HttpStatus.UNAUTHORIZED, ex.getMessage(), "CommonAuthException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                HttpStatus.UNAUTHORIZED,
                ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CommonBadRequestException.class)
    public CommonErrorResponse handleCommonBadRequestException(CommonBadRequestException ex) {
        addErrorLog(HttpStatus.BAD_REQUEST, ex.getMessage(), "CommonBadRequestException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidInputException.class)
    public CommonErrorResponse handleInvalidInputException(InvalidInputException ex) {
        String message = messageSourceUtil.getMessage(ex.getMessage(), ex.messageArguments());
        addErrorLog(HttpStatus.BAD_REQUEST.value(), message, "InvalidInputException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                HttpStatus.BAD_REQUEST.value(),
                message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public CommonErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        addErrorLog(HttpStatus.BAD_REQUEST, ex.getMessage(), "MethodArgumentTypeMismatchException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public CommonErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {

        Optional<String> violations = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + messageSourceUtil.getMessage(v.getMessage()))
                .collect(collectingAndThen(joining("; "), Optional::ofNullable));

        String errMsg = violations.orElse(ex.getMessage());
        addErrorLog(HttpStatus.BAD_REQUEST, errMsg, "ConstraintViolationException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                HttpStatus.BAD_REQUEST,
                errMsg);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CommonException.class)
    public CommonErrorResponse handleCommonException(CommonException ex) {
        addErrorLog(ex.getCode(), ex.getMessage(), ex);
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CommonErrorResponse handleAll(Exception ex) {
        addErrorLog(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        String errMsg = "Unexpected internal server error occurs";
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                errMsg/*ex.getMessage()*/);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Optional<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> String.format("'%s' %s", x.getField(), messageSourceUtil.getMessage(x.getDefaultMessage())
                ))
                .collect(collectingAndThen(joining("; "), Optional::ofNullable));

        String errLogMessage = String.join(System.lineSeparator(),
                errors.orElse(ex.getMessage()),
                "Request body: " + webUtil.getRequestBody());

        addErrorLog(status, errLogMessage, "MethodArgumentNotValidException");
        CommonErrorResponse commonErrorResponse = new CommonErrorResponse(
                webUtil.getRequestId(),
                status,
                errors.orElse(ex.getMessage()));
        return new ResponseEntity<>(commonErrorResponse, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        addErrorLog(status, error, "MissingServletRequestParameterException");
        CommonErrorResponse commonErrorResponse = new CommonErrorResponse(
                webUtil.getRequestId(),
                status,
                error);
        return new ResponseEntity<>(commonErrorResponse, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String errLogMessage = String.join(System.lineSeparator(),
                ex.getMessage(),
                "Request body: " + webUtil.getRequestBody());

        String error = "Request not readable";
        addErrorLog(status, errLogMessage, "HttpMessageNotReadableException");
        CommonErrorResponse commonErrorResponse =
                new CommonErrorResponse(webUtil.getRequestId(), status, /*error*/ex.getMessage());
        return new ResponseEntity<>(commonErrorResponse, headers, status);
    }


    /*** Logging ***/

    protected void addErrorLog(HttpStatus httpStatus, String errorMessage, Throwable ex) {
        addErrorLog(httpStatus.value(), errorMessage, ex);
    }

    protected void addErrorLog(HttpStatus httpStatus, String errorMessage, String exceptionType) {
        addErrorLog(httpStatus.value(), errorMessage, exceptionType);
    }

    protected void addErrorLog(Integer errorCode, String errorMessage, Throwable ex) {
        log.error("[Error] | Code: {} | Type: {} | Path: {} | Elapsed time: {} ms | Message: {}",
                errorCode, ex.getClass().getTypeName(), webUtil.getRequestUri(),
                v("elapsed_time", webUtil.getElapsedTime()), errorMessage, ex);
    }

    protected void addErrorLog(Integer errorCode, String errorMessage, String exceptionType) {
        log.error("[Error] | Code: {} | Type: {} | Path: {} | Elapsed time: {} ms | Message: {}",
                errorCode, exceptionType, webUtil.getRequestUri(),
                v("elapsed_time", webUtil.getElapsedTime()), errorMessage);
    }

}
