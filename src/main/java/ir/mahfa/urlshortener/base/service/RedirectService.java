package ir.mahfa.urlshortener.base.service;

import ir.mahfa.urlshortener.url.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Log4j2
@Service
@AllArgsConstructor
public class RedirectService {
    private final UrlService urlService;

    public String getRedirectResponse(String urlKey, HttpServletRequest request) throws URISyntaxException {
        log.info("getRedirectResponse from {}}", urlKey);
        String destination = urlService.getUrl(urlKey);
        URIBuilder b = new URIBuilder(destination);
        request.getParameterNames()
                .asIterator()
                .forEachRemaining(s -> b.addParameter(s, request.getParameter(s)));
        return b.build().toString();
    }
}
