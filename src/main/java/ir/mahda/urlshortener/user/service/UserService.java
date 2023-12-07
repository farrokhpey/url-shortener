package ir.mahda.urlshortener.user.service;

import ir.mahda.urlshortener.base.exception.BusinessServiceException;
import ir.mahda.urlshortener.base.exception.enums.ErrorCode;
import ir.mahda.urlshortener.user.User;
import ir.mahda.urlshortener.user.dto.request.NewUserRequestDto;
import ir.mahda.urlshortener.user.dto.response.UserResponseDto;
import ir.mahda.urlshortener.user.repository.UserRepository;
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

    public User findByUsername(String currentUserName) {
        return repository.findByUsername(currentUserName)
                .orElseThrow(() -> new BusinessServiceException(ErrorCode.USER_DOES_NOT_EXIST));
    }
}
