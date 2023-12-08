package ir.mahfa.urlshortener.url.service;
import ir.mahfa.urlshortener.UrlShortenerApplication;
import ir.mahfa.urlshortener.base.SecurityUtils;
import ir.mahfa.urlshortener.base.exception.BusinessServiceException;
import ir.mahfa.urlshortener.base.exception.enums.ErrorCode;
import ir.mahfa.urlshortener.base.service.RedisUrlService;
import ir.mahfa.urlshortener.url.dto.request.NewUrlRequestDto;
import ir.mahfa.urlshortener.url.dto.response.UrlResponseDto;
import ir.mahfa.urlshortener.url.repository.UrlRepository;
import ir.mahfa.urlshortener.user.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@Import(FlywayAutoConfiguration.class)
@SpringBootTest(classes = UrlShortenerApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UrlServiceTest {

    @Autowired
    UrlRepository urlRepository;

    @Autowired
    UserService userService;

    @Autowired
    RedisUrlService redisUrlService;

    private static final String TEST_URL = "http://test";
    private static final String TEST_USER_NAME = "testUser";
    private static final Object SHORTED_TEST_URL = "shorten_test";

    static MockedStatic<SecurityUtils> securityUtils;

    @BeforeAll
    static void beforeAll() {
        securityUtils = Mockito.mockStatic(SecurityUtils.class);
        securityUtils
                .when(SecurityUtils::getCurrentUserLoggedInUsername)
                .thenReturn(Optional.of(TEST_USER_NAME));
    }

    @AfterAll
    static void afterAll() {
        securityUtils.close();
    }

    @Test
    @DisplayName("user should be able to add normal values")
    void simpleAdd() {
        MockedStatic<RandomStringUtils> randomStringUtils = Mockito.mockStatic(RandomStringUtils.class);
        randomStringUtils
                .when(() -> RandomStringUtils.randomAlphabetic(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(SHORTED_TEST_URL);

        UrlService urlService = new UrlService(urlRepository, redisUrlService, userService);
        var shortedUrl = urlService.add(new NewUrlRequestDto(TEST_URL));

        Assertions.assertEquals(SHORTED_TEST_URL, shortedUrl);

        randomStringUtils.close();
    }

    @Test
    @DisplayName("user should get an error while adding duplicate value")
    void duplicateAdd() {
        MockedStatic<RandomStringUtils> randomStringUtils = Mockito.mockStatic(RandomStringUtils.class);
        randomStringUtils
                .when(() -> RandomStringUtils.randomAlphabetic(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(SHORTED_TEST_URL);

        UrlService urlService = new UrlService(urlRepository, redisUrlService, userService);
        urlService.add(new NewUrlRequestDto(TEST_URL));

        var exception = Assertions.assertThrows(BusinessServiceException.class, () -> urlService.add(new NewUrlRequestDto(TEST_URL)));
        Assertions.assertEquals(ErrorCode.DUPLICATE_KEY, exception.getErrorCode());
        randomStringUtils.close();
    }

    @Test
    @DisplayName("user should get an error while adding more than 10 urls")
    void maximumReachedAdd() {
        UrlService urlService = new UrlService(urlRepository, redisUrlService, userService);
        for (int urlCount = 0; urlCount < 10; urlCount++) {
            int finalUrlCount = urlCount;
            Assertions.assertDoesNotThrow(() -> urlService.add(new NewUrlRequestDto(TEST_URL + "_" + finalUrlCount)));
        }
        var exception = Assertions.assertThrows(BusinessServiceException.class,
                () -> urlService.add(new NewUrlRequestDto(TEST_URL + "_" + urlService)));
        Assertions.assertEquals(ErrorCode.MAXIMUM_REACHED, exception.getErrorCode());
    }

    @Test
    @DisplayName("user should be able to delete any of its urls")
    void simpleDelete() {
        UrlService urlService = new UrlService(urlRepository, redisUrlService, userService);
        String urlKey = urlService.add(new NewUrlRequestDto(TEST_URL));
        var exception = Assertions.assertThrows(BusinessServiceException.class, () -> urlService.add(new NewUrlRequestDto(TEST_URL)));
        Assertions.assertEquals(ErrorCode.DUPLICATE_KEY, exception.getErrorCode());
        Assertions.assertDoesNotThrow(() -> urlService.delete(urlKey));
        Assertions.assertDoesNotThrow(() -> urlService.add(new NewUrlRequestDto(TEST_URL)));
    }

    @Test
    @DisplayName("user should be able to delete any of its urls")
    void failDelete() {
        String otherUrl = "ThinBC";
        UrlService urlService = new UrlService(urlRepository, redisUrlService, userService);
        var exception = Assertions.assertThrows(BusinessServiceException.class, () -> urlService.delete(otherUrl));
        Assertions.assertEquals(ErrorCode.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("user should be able to get all of its urls")
    void getAll() {
        UrlService urlService = new UrlService(urlRepository, redisUrlService, userService);
        for (int urlCount = 0; urlCount < 5; urlCount++) {
            int finalUrlCount = urlCount;
            Assertions.assertDoesNotThrow(() -> urlService.add(new NewUrlRequestDto(TEST_URL + "_" + finalUrlCount)));
        }
        List<UrlResponseDto> allUrls = urlService.getAll();
        Assertions.assertNotNull(allUrls);
        Assertions.assertEquals(5, allUrls.size());
    }

    @Test
    @DisplayName("user should be able to get any of its urls")
    void getUrl() {
        UrlService urlService = new UrlService(urlRepository, redisUrlService, userService);
        String urlKey = urlService.add(new NewUrlRequestDto(TEST_URL));
        String url = urlService.getUrl(urlKey);
        Assertions.assertEquals(TEST_URL, url);
    }
}
