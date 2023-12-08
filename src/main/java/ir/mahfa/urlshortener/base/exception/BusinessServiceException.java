package ir.mahfa.urlshortener.base.exception;

import ir.mahfa.urlshortener.base.exception.enums.ErrorCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
public class BusinessServiceException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessServiceException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}