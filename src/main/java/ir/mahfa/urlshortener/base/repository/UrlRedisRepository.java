package ir.mahfa.urlshortener.base.repository;

import ir.mahfa.urlshortener.url.Url;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UrlRedisRepository {

    private final RedisTemplate<String, String> urlRedisTemplate;
    @Value(value = "${application.redis_url.days_validity}")
    private int daysValidity;

    public void save(Url url) {
        urlRedisTemplate
                .opsForValue()
                .set(url.getUrlKey(), url.getDestination(), Duration.ofDays(daysValidity));
    }

    public void delete(String urlKey) {
        urlRedisTemplate.opsForValue()
                .getAndDelete(urlKey);
    }

    public String find(String urlKey) {
        return urlRedisTemplate
                .opsForValue()
                .get(urlKey);
    }

}
