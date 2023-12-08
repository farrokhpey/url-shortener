package ir.mahfa.urlshortener.url.dto.response;

import java.time.LocalDate;

public record UrlResponseDto(String urlKey, String destination, int views, LocalDate lastUse) {
}
