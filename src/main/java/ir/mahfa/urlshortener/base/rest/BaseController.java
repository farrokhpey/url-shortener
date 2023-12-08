package ir.mahfa.urlshortener.base.rest;

import ir.mahfa.urlshortener.base.service.RedirectService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@Profile("viewer")
@AllArgsConstructor
@RequestMapping("/")
public class BaseController {

    private final RedirectService redirectService;

    @GetMapping("{urlKey}")
    public void redirect(@PathVariable String urlKey, HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException {
        response.sendRedirect(redirectService.getRedirectResponse(urlKey, request));
    }
}

