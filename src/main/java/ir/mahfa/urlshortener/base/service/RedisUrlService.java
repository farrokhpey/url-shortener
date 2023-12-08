package ir.mahfa.urlshortener.base.service;

import ir.mahfa.urlshortener.base.repository.UrlRedisRepository;
import ir.mahfa.urlshortener.url.Url;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class RedisUrlService {


    private final UrlRedisRepository urlRedisRepository;

    public void save(Url url) {
        log.info("RedisUrlService, saving {} in redis", url.getDestination());
        urlRedisRepository.save(url);
    }

    public void delete(String urlKey) {
        log.info("RedisUrlService, deleting {} from redis", urlKey);
        urlRedisRepository.delete(urlKey);
    }

    public Optional<String> find(String urlKey) {
        log.info("RedisUrlService, getting {} from redis", urlKey);
        return Optional.ofNullable(urlRedisRepository.find(urlKey));
    }
}
