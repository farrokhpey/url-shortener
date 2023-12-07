package ir.mahda.urlshortener.base.exception.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum ErrorCode {
    USER_ALREADY_EXIST(1001, HttpStatus.BAD_REQUEST, "User already exist"),
    USER_DOES_NOT_EXIST(1002, HttpStatus.BAD_REQUEST, "User does not exist"),
    IO_EXCEPTION(9001, HttpStatus.BAD_REQUEST, "An error occurred"),
    INVALID_INPUT_EXCEPTION(4001, HttpStatus.BAD_REQUEST, "Invalid input exception."),
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "Server runtime exception"),
    RESOURCE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Resource not found"),
    JSON_PROCESSING_EXCEPTION(5002, HttpStatus.INTERNAL_SERVER_ERROR, "Json processing exception");


    @JsonIgnore
    public final HttpStatus httpStatus;
    @JsonProperty("code")
    private final int code;
    @JsonProperty("message")
    private final String message;

    ErrorCode(@JsonProperty("code") int code, HttpStatus httpStatus, @JsonProperty("message") String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @JsonCreator
    public static Optional<ErrorCode> forValues(@JsonProperty("code") int code,
                                                @JsonProperty("message") String message) {
        return Arrays.stream(ErrorCode.values())
                .filter(errorCode -> code == errorCode.getCode() && errorCode.getMessage().equals(message))
                .findFirst();
    }

}