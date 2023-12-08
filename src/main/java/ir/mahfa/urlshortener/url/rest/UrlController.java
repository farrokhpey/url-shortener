package ir.mahfa.urlshortener.url.rest;

import ir.mahfa.urlshortener.base.ApiResponseDTO;
import ir.mahfa.urlshortener.url.dto.request.NewUrlRequestDto;
import ir.mahfa.urlshortener.url.dto.response.UrlResponseDto;
import ir.mahfa.urlshortener.url.service.UrlService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Profile("user")
@AllArgsConstructor
@RequestMapping("/url")
public class UrlController {
    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<String>> create(@Valid @RequestBody NewUrlRequestDto dto) {
        return ResponseEntity.ok(ApiResponseDTO.<String>builder()
                .data(urlService.add(dto))
                .succeed(true)
                .errorCode(null)
                .build());
    }

    @DeleteMapping
    public void delete(String urlKey) {
        urlService.delete(urlKey);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<UrlResponseDto>>> getAll() {
        return ResponseEntity.ok(ApiResponseDTO.<List<UrlResponseDto>>builder()
                .data(urlService.getAll())
                .succeed(true)
                .errorCode(null)
                .build());
    }
}

