package ir.mahfa.urlshortener.base;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtils {
    public static Optional<String> getCurrentUserLoggedInUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return
                Optional
                        .ofNullable(securityContext.getAuthentication())
                        .map(authentication -> {
                            if (authentication.getPrincipal() instanceof UserDetails) {
                                UserDetails userDetail = (UserDetails) authentication.getPrincipal();
                                return userDetail.getUsername();
                            } else if (authentication.getPrincipal() instanceof String) {
                                return (String) authentication.getPrincipal();
                            }
                            return null;
                        });

    }

}