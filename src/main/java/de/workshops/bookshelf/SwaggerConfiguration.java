package de.workshops.bookshelf;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class SwaggerConfiguration {

    @Bean
    public OpenAPI api(ApplicationProperties applicationProperties) {
        return new OpenAPI()
                .info(
                        new Info()
                                .title(applicationProperties.getTitle())
                                .version(applicationProperties.getVersion())
                                .license(new License()
                                        .name(applicationProperties.getLicense().getName())
                                        .url(applicationProperties.getLicense().getLink().toString())
                                )
                );
    }
}
