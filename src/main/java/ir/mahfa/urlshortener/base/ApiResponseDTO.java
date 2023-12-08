package ir.mahfa.urlshortener.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.mahfa.urlshortener.base.exception.enums.ErrorCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@SuperBuilder
@Jacksonized
public class ApiResponseDTO<D> {

    @JsonProperty("data")
    private D data;

    @JsonProperty("succeed")
    private boolean succeed;

    @JsonProperty("error_code")
    private ErrorCode errorCode;
}
