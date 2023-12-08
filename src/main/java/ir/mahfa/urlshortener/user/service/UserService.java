package ir.mahfa.urlshortener.user.service;

import ir.mahfa.urlshortener.base.exception.BusinessServiceException;
import ir.mahfa.urlshortener.base.exception.enums.ErrorCode;
import ir.mahfa.urlshortener.user.User;
import ir.mahfa.urlshortener.user.dto.request.NewUserRequestDto;
import ir.mahfa.urlshortener.user.dto.response.UserResponseDto;
import ir.mahfa.urlshortener.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserResponseDto addUser(NewUserRequestDto dto) {
        log.info("UserService, creating new user {} ", dto.username());
        if (repository.findByUsername(dto.username()).isPresent()) {
            throw new BusinessServiceException(ErrorCode.USER_ALREADY_EXIST);
        }
        String encryptedPassword = encoder.encode(dto.password());
        User user = User.builder()
                .username(dto.username())
                .password(encryptedPassword)
                .build();
        repository.save(user);
        return new UserResponseDto(dto.username());
    }

    public User findByUsername(String username) {
        log.info("UserService, finding user {} ", username);
        return repository.findByUsername(username)
                .orElseThrow(() -> new BusinessServiceException(ErrorCode.USER_DOES_NOT_EXIST));
    }
}
