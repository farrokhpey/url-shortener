package ir.mahfa.urlshortener.url.repository;

import ir.mahfa.urlshortener.url.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, String> {
    Optional<Url> findByUrlKey(String urlKey);

    Optional<Url> findByDestinationAndUser_Username(String destination, String username);

    Optional<Url> findByUrlKeyAndUser_Username(String urlKey, String user_username);

    List<Url> findByUser_Username(String username);

    List<Url> findByLastUseBefore(LocalDate lastUse);

    void deleteAllByUrlKeyIn(Collection<String> urlKey);
}
