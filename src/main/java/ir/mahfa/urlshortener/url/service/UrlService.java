package ir.mahfa.urlshortener.url.service;

import ir.mahfa.urlshortener.base.SecurityUtils;
import ir.mahfa.urlshortener.base.exception.BusinessServiceException;
import ir.mahfa.urlshortener.base.exception.enums.ErrorCode;
import ir.mahfa.urlshortener.url.Url;
import ir.mahfa.urlshortener.url.dto.request.NewUrlRequestDto;
import ir.mahfa.urlshortener.url.dto.response.UrlResponseDto;
import ir.mahfa.urlshortener.url.repository.UrlRepository;
import ir.mahfa.urlshortener.user.User;
import ir.mahfa.urlshortener.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor
public class UrlService {
    private static final int MIN_URLKEY_LENGTH = 5;
    private static final int MAX_URLKEY_LENGTH = 9;

    private final UrlRepository urlRepository;
    private final UserService userService;

    public String add(NewUrlRequestDto dto) {
        User currentUser = getCurrentUser();
        checkDuplicateDestinationInDB(dto, currentUser);
        checkMaximumAllowedKeys(currentUser);
        return saveNewUrl(dto, currentUser);
    }

    public void delete(String urlKey) {
        User currentUser = getCurrentUser();
        Url url = urlRepository.findByUrlKeyAndUser_Username(urlKey, currentUser.getUsername())
                .orElseThrow(() -> new BusinessServiceException(ErrorCode.RESOURCE_NOT_FOUND));
        urlRepository.delete(url);
    }

    public List<UrlResponseDto> getAll() {
        User currentUser = getCurrentUser();
        return urlRepository.findByUser_Username(currentUser.getUsername()).stream()
                .map(url -> new UrlResponseDto(url.getUrlKey(),
                        url.getDestination(),
                        url.getViews(),
                        url.getLastUse()))
                .collect(Collectors.toList());
    }

    public String getUrl(String urlKey) {
        String destination = getUrlFromDB(urlKey).getDestination();
        updateUrlData(urlKey);
        return destination;
    }

    private void updateUrlData(String urlKey) {
        new Thread(() -> {
            Url url = getUrlFromDB(urlKey);
            url.addViews();
            url.setLastUse(LocalDate.now());
            urlRepository.save(url);
        }).start();
    }

    private Url getUrlFromDB(String urlKey) {
        return urlRepository.findByUrlKey(urlKey)
                .orElseThrow(() -> new BusinessServiceException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    private User getCurrentUser() {
        String currentUserName = SecurityUtils.getCurrentUserLoggedInUsername()
                .orElseThrow(() -> new BusinessServiceException(ErrorCode.USER_DOES_NOT_EXIST));
        return userService.findByUsername(currentUserName);
    }

    private String generateShortLink() {
        String generated = RandomStringUtils.randomAlphabetic(MIN_URLKEY_LENGTH, MAX_URLKEY_LENGTH);
        while (urlRepository.findByUrlKey(generated).isPresent()) {
            generated = RandomStringUtils.randomAlphabetic(MIN_URLKEY_LENGTH, MAX_URLKEY_LENGTH);
        }
        return generated;
    }

    private void checkMaximumAllowedKeys(User currentUser) {
        List<Url> urls = urlRepository.findByUser_Username(currentUser.getUsername());
        if (!CollectionUtils.isEmpty(urls) && urls.size() == 10) {
            throw new BusinessServiceException(ErrorCode.MAXIMUM_REACHED);
        }
    }

    private void checkDuplicateDestinationInDB(NewUrlRequestDto dto, User currentUser) {
        if (urlRepository.findByDestinationAndUser_Username(dto.destination(), currentUser.getUsername()).isPresent()) {
            throw new BusinessServiceException(ErrorCode.DUPLICATE_KEY);
        }
    }

    private String saveNewUrl(NewUrlRequestDto dto, User currentUser) {
        String urlKey = generateShortLink();
        Url url = Url.builder()
                .urlKey(urlKey)
                .destination(dto.destination())
                .views(0)
                .lastUse(LocalDate.now())
                .user(currentUser)
                .build();
        urlRepository.save(url);
        return urlKey;
    }
}