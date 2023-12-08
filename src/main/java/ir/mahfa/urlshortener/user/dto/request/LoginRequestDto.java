package ir.mahfa.urlshortener.user.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public final class LoginRequestDto {

    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "password")
    private String password;

}
