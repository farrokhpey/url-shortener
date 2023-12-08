package ir.mahfa.urlshortener.base.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.annotations.OpenAPI31;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPI31
public class OpenAPIConfiguration {
    @Value("${server.port}")
    private long port;

    @Bean
    public OpenAPI openApiInformation() {
        Server localServer = new Server()
                .url("http://localhost:" + port)
                .description("Localhost Server URL");

        Contact contact = new Contact()
                .email("farrokhpey@gmail.com")
                .name("Mahdi Farrokh");

        Info info = new Info()
                .contact(contact)
                .description("URL Shortener Application")
                .summary("Simple Samp[e URL Shortener using Java SpringBoot")
                .title("URL Shortener")
                .version("V1.0.0")
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));

        return new OpenAPI()
                .info(info)
                .addSecurityItem(new SecurityRequirement()
                        .addList("JWT_TOKEN"))
                .components(new Components()
                        .addSecuritySchemes("JWT_TOKEN", new SecurityScheme()
                                .name("JWT_TOKEN")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")))
                .addServersItem(localServer);
    }
}
