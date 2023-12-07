package ir.mahda.urlshortener.user.rest;

import ir.mahda.urlshortener.base.ApiResponseDTO;
import ir.mahda.urlshortener.base.exception.enums.ErrorCode;
import ir.mahda.urlshortener.base.service.JwtService;
import ir.mahda.urlshortener.user.dto.request.LoginRequestDto;
import ir.mahda.urlshortener.user.dto.request.NewUserRequestDto;
import ir.mahda.urlshortener.user.dto.response.UserResponseDto;
import ir.mahda.urlshortener.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Profile("user")
@AllArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService service;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<UserResponseDto>> addNewUser(@Valid @RequestBody NewUserRequestDto user) throws Exception {
        return ResponseEntity.ok(ApiResponseDTO.<UserResponseDto>builder()
                .data(service.addUser(user))
                .succeed(true)
                .errorCode(null)
                .build());
    }

    @PostMapping("/token")
    public ResponseEntity<ApiResponseDTO<String>> authenticateAndGetToken(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok(ApiResponseDTO.<String>builder()
                    .data(jwtService.generateToken(loginRequestDto.getUsername()))
                    .succeed(true)
                    .errorCode(null)
                    .build());
        } else {
            return ResponseEntity.ok(ApiResponseDTO.<String>builder()
                    .data("User not found.")
                    .succeed(false)
                    .errorCode(ErrorCode.RESOURCE_NOT_FOUND)
                    .build());
        }
    }
}
