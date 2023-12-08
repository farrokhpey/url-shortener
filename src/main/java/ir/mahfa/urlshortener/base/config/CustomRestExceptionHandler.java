package ir.mahfa.urlshortener.base.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import ir.mahfa.urlshortener.base.ApiResponseDTO;
import ir.mahfa.urlshortener.base.exception.BusinessServiceException;
import ir.mahfa.urlshortener.base.exception.enums.ErrorCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import java.util.stream.Collectors;
@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {
    private static final int MAX_ERROR_DESCRIPTION_CHARACTER_IN_UN_KNOWN_ERROR = 50;
    private static final Logger LOGGER = LogManager.getLogger();

    @ExceptionHandler(BusinessServiceException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleRuntimeException(BusinessServiceException exception) {
        return new ResponseEntity<>(
                ApiResponseDTO.builder().data(null).succeed(false).errorCode(exception.getErrorCode()).build(),
                exception.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(value = {JsonProcessingException.class})
    public ResponseEntity<Object> handleJsonProcessingException(JsonProcessingException exception) {
        return fromError(ErrorCode.JSON_PROCESSING_EXCEPTION);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException exception) {
        return fromError(ErrorCode.JSON_PROCESSING_EXCEPTION);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        String uri = "";
        if (request instanceof ServletWebRequest servletWebRequest) {
            uri = ((ServletWebRequest) request).getRequest().getRequestURI();
            var response = servletWebRequest.getResponse();
            if (response != null && response.isCommitted()) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Response already committed. Ignoring: " + ex);
                }
                return null;
            }
        }
        if (statusCode.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        var specificCause = NestedExceptionUtils.getMostSpecificCause(ex);

        if (specificCause instanceof MethodArgumentNotValidException methodArgumentNotValidException
                && methodArgumentNotValidException.getBindingResult() != null) {
            return fromBindingResult(methodArgumentNotValidException.getBindingResult(), uri);
        }

        var errorMessage = specificCause.getMessage();
        if (ex instanceof ErrorResponse errorResponse && errorResponse.getBody() != null) {
            errorMessage = errorResponse.getBody().getDetail();
        }
        errorMessage = (ObjectUtils.isEmpty(errorMessage) || errorMessage.length() <= MAX_ERROR_DESCRIPTION_CHARACTER_IN_UN_KNOWN_ERROR) ?
                errorMessage : errorMessage.substring(0, MAX_ERROR_DESCRIPTION_CHARACTER_IN_UN_KNOWN_ERROR).concat(" ...");
        var errorBody = ApiResponseDTO.builder()
                .errorCode(ErrorCode.INVALID_INPUT_EXCEPTION)
                .succeed(Boolean.FALSE)
                .build();
        logger.warn(errorMessage + " from: " + uri);
        return new ResponseEntity<>(errorBody, ErrorCode.INVALID_INPUT_EXCEPTION.httpStatus);
    }

    @Override
    protected ResponseEntity<Object> createResponseEntity(@Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        return new ResponseEntity(body, headers, statusCode);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnKnownException(Exception exception) {
        LOGGER.error("UnKnown Exception: ", exception);
        return fromError(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> fromBindingResult(BindingResult bindingResult, String uri) {
        var errorMessage = ErrorCode.INVALID_INPUT_EXCEPTION.getMessage();

        if (!ObjectUtils.isEmpty(bindingResult.getAllErrors())) {
            errorMessage = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
        }
        logger.warn(errorMessage + " from: " + uri);
        return fromError(ErrorCode.INVALID_INPUT_EXCEPTION, errorMessage);
    }

    private ResponseEntity<Object> fromError(ErrorCode error) {
        var errorBody = ApiResponseDTO.builder()
                .errorCode(error)
                .succeed(Boolean.FALSE)
                .build();
        return new ResponseEntity<>(errorBody, error.httpStatus);
    }

    private ResponseEntity<Object> fromError(ErrorCode error, String errorMessage) {
        var errorBody = ApiResponseDTO.builder()
                .errorCode(error)
                .succeed(Boolean.FALSE)
                .build();
        logger.warn(error.getMessage());
        return new ResponseEntity<>(errorBody, error.httpStatus);
    }
}