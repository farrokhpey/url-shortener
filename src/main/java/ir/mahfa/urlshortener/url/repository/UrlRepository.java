package ir.mahfa.urlshortener.url.repository;

import ir.mahfa.urlshortener.url.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Url url SET url.views = url.views+1, url.lastUse= CURRENT_DATE where url.urlKey = ?1")
    void updateViewsAndDateAndGet(String urlKey);
    void deleteAllByUrlKeyIn(Collection<String> urlKey);
}
